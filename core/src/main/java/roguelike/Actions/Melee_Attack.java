package roguelike.Actions;

import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.engine.Message_Log;
import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;
import roguelike.Systems.Death_System;
import roguelike.utilities.Roll;

import static roguelike.Generation.World.entityManager;

public class Melee_Attack extends Action{

	public Integer attacker;
	public Integer target;

	public Melee_Attack(Integer attacker, Integer target){
		this.attacker = attacker;
		this.target = target;
		this.cost = 1000;
	}

	@Override
	public boolean perform() {

		if (entityManager.gc(attacker, Energy.class).energy < cost)
			return true;
		entityManager.gc(attacker, Energy.class).energy -= cost;
		Position position = entityManager.gc(attacker, Position.class);
		if (position == null)
			return true;
		Coord location = position.location;
		Position aimPosition = entityManager.gc(target, Position.class);
		if (aimPosition == null)
			return true;
		Coord aim = aimPosition.location;
		Sprite sprite = entityManager.gc(attacker, Sprite.class);
		if(sprite != null && sprite.glyph != null) 
			entityManager.display.bump(0f, sprite.glyph,
				Direction.getDirection(location.x - aim.x, location.y - aim.y), 0.15f, null);
		int damage = 0;
		String type;
		Equipment targetEquipment = entityManager.gc(target, Equipment.class);
		Equipment equipment = entityManager.gc(attacker, Equipment.class);
		if (equipment == null || targetEquipment == null)
			return true;
		for (Damage dam : equipment.get_melee_damages()) {
			String[] types = dam.type.split("/");
			type = types[Roll.rand(0, types.length - 1)];
			int defensive_amount = targetEquipment.get_resistance_from_type(type);
			damage += dam.roll() - defensive_amount;

			if (damage < 0) {
				damage = 0;
			}
		}

		Message_Log.getInstance().add_formatted_message("attack", attacker, target, damage);

		new Death_System(attacker, target, "health", -damage).process();

		entityManager.gc(attacker, Action_Component.class).setAction(null);

		return true;
	}
}
