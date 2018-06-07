package roguelike.Actions;

import roguelike.Components.Action_Component;
import roguelike.Components.Energy;

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

		System.out.println("Successful attack!");

		entityManager.gc(attacker, Energy.class).energy -= cost;
		entityManager.gc(attacker, Action_Component.class).setAction(null);

		return true;
	}
}
