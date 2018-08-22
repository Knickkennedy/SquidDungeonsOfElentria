package roguelike.Actions;

import roguelike.Actions.Animations.Animation;
import roguelike.Actions.Animations.MeleeAttackAnimation;
import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.Systems.DeathSystem;
import roguelike.engine.MessageLog;
import roguelike.utilities.Roll;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;

import java.util.List;

import static roguelike.Generation.World.entityManager;

public class MeleeAttack extends Action{

	private Integer attacker;
	private Integer target;
	private SparseLayers display;
	private List<Animation>animations;

	public MeleeAttack(Integer attacker, Integer target, SparseLayers display, List<Animation> animations){
		this.attacker = attacker;
		this.target = target;
		this.cost = 1000;
		this.display = display;
		this.animations = animations;
	}

	@Override
	public boolean canPerform(){
		return entityManager.gc(attacker, Energy.class).energy >= cost;
	}

	@Override
	public boolean perform() {

		Statistics statistics = entityManager.gc(target, Statistics.class);
		if(statistics != null) {
			Sprite sprite = entityManager.gc(attacker, Sprite.class);
			Coord attackerLocation = entityManager.gc(attacker, Position.class).location;
			Coord defenderLocation = entityManager.gc(target, Position.class).location;
			animations.add(new MeleeAttackAnimation(sprite, Direction.getDirection(defenderLocation.x - attackerLocation.x, defenderLocation.y - attackerLocation.y)));

			int damage = 0;
			String type;

			Equipment attackerEquipment = entityManager.gc(attacker, Equipment.class);
			Equipment defenderEquipment = entityManager.gc(target, Equipment.class);
			if(attackerEquipment != null && defenderEquipment != null) {
				MeleeModifiers meleeModifiers = attackerEquipment.getMeleeModifiers();

				for (Damage dam : attackerEquipment.get_melee_damages()) {
					String[] types = dam.type.split("/");
					int number_of_types = types.length;

					type = types[Roll.rand(0, number_of_types - 1)];
					int defensive_amount = defenderEquipment.get_resistance_from_type(type);
					damage += dam.roll() + meleeModifiers.damageBonus - defensive_amount;

					if (damage < 0) {
						damage = 0;
					}
				}
			}
			else if(attackerEquipment != null){
				Creature creature = entityManager.gc(target, Creature.class);
				MeleeModifiers meleeModifiers = attackerEquipment.getMeleeModifiers();

				for (Damage dam : attackerEquipment.get_melee_damages()) {
					String[] types = dam.type.split("/");
					int number_of_types = types.length;

					type = types[Roll.rand(0, number_of_types - 1)];
					int defensive_amount = creature.get_resistance_from_type(type);
					damage += dam.roll() + meleeModifiers.damageBonus - defensive_amount;

					if (damage < 0) {
						damage = 0;
					}
				}
			}
			else if(defenderEquipment != null){
				Creature creature = entityManager.gc(attacker, Creature.class);
				MeleeModifiers meleeModifiers = creature.meleeModifiers;

				for(Damage dam : creature.attack.damages){
					String[] types = dam.type.split("/");
					int number_of_types = types.length;

					type = types[Roll.rand(0, number_of_types - 1)];
					int defensive_amount = defenderEquipment.get_resistance_from_type(type);
					damage += dam.roll() + meleeModifiers.damageBonus - defensive_amount;

					if (damage < 0) {
						damage = 0;
					}
				}
			}

			MessageLog.getInstance().add_formatted_message("attack", attacker, target, damage);

			new DeathSystem(attacker, target, "health", - damage).process();

			entityManager.gc(attacker, Energy.class).energy -= cost;
			entityManager.gc(attacker, ActionComponent.class).setAction(null);

			return true;
		}
		else{
			entityManager.gc(attacker, ActionComponent.class).setAction(null);

			return true;
		}
	}

	@Override
	public boolean isAlternativeAction() {
		return false;
	}
}
