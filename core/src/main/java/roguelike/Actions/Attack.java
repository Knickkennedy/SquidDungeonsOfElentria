package roguelike.Actions;

import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.Enums.Equipment_Slot;
import roguelike.engine.Message_Log;

import static roguelike.Generation.World.entityManager;

public class Attack extends Action{

	public Integer attacker;
	public Integer target;

	public Attack(Integer attacker, Integer target){
		this.attacker = attacker;
		this.target = target;
		this.cost = 1000;
	}

	@Override
	public boolean perform() {

		if(entityManager.gc(attacker, Energy.class).energy < cost)
			return true;

		if(entityManager.gc(attacker, Equipment.class).equipment.get(Equipment_Slot.LEFT_HAND) != null){
			int damage = 0;

			for(Damage dam : entityManager.gc(entityManager.gc(attacker, Equipment.class).equipment.get(Equipment_Slot.LEFT_HAND), Offensive_Component.class).damages){
				damage += dam.roll();
			}

			Message_Log.getInstance().add_formatted_message("attack", attacker, target, damage);

			entityManager.gc(target, Statistics.class).health.current_value -= damage;

		}

		entityManager.gc(attacker, Energy.class).energy -= cost;
		entityManager.gc(attacker, Action_Component.class).setAction(null);

		return true;
	}
}
