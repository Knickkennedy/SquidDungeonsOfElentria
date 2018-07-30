package roguelike.Actions;

import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.Systems.Death_System;
import roguelike.engine.Message_Log;
import roguelike.utilities.Roll;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;

import static roguelike.Generation.World.entityManager;

public class Melee_Attack extends Action{

	private Integer attacker;
	private Integer target;
	private SparseLayers display;

	public Melee_Attack(Integer attacker, Integer target, SparseLayers display){
		this.attacker = attacker;
		this.target = target;
		this.cost = 1000;
		this.display = display;
	}

	@Override
	public boolean perform() {

		if(entityManager.gc(attacker, Energy.class).energy < cost)
			return true;

		Sprite sprite = entityManager.gc(attacker, Sprite.class);
		Coord attackerLocation = entityManager.gc(attacker, Position.class).location;
		Coord defenderLocation = entityManager.gc(target, Position.class).location;
		if(sprite.getGlyph() != null){
			display.bump(sprite.getGlyph(), Direction.getDirection(defenderLocation.x - attackerLocation.x, defenderLocation.y - attackerLocation.y), 0.05f);
		}

		int damage = 0;
		String type;

		for(Damage dam : entityManager.gc(attacker, Equipment.class).get_melee_damages()){
			String[] types = dam.type.split("/");
			int number_of_types = types.length;

			type = types[Roll.rand(0, number_of_types - 1)];
			int defensive_amount = entityManager.gc(target, Equipment.class).get_resistance_from_type(type);
			damage += dam.roll() - defensive_amount;

			if(damage < 0){
				damage = 0;
			}
		}

		Message_Log.getInstance().add_formatted_message("attack", attacker, target, damage);

		new Death_System(attacker, target, "health", -damage).process();

		entityManager.gc(attacker, Energy.class).energy -= cost;
		entityManager.gc(attacker, Action_Component.class).setAction(null);

		return true;
	}
}
