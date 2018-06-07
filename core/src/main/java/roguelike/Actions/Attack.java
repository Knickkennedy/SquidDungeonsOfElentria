package roguelike.Actions;

import roguelike.Components.Action_Component;
import roguelike.Components.Energy;
import roguelike.Components.Position;

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

		System.out.println(entityManager.gc(attacker, Position.class).location);
		System.out.println(entityManager.gc(target, Position.class).location);

		entityManager.gc(attacker, Energy.class).energy -= cost;
		entityManager.gc(attacker, Action_Component.class).setAction(null);

		return true;
	}
}
