package roguelike.Systems;

import roguelike.Actions.Action;
import roguelike.Components.Action_Component;
import roguelike.Components.Active;
import roguelike.Components.Energy;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class Turn_System implements System {

	@Override
	public int process(int current_actor) {
		ArrayList<Integer> actors = new ArrayList<>(entityManager.getAllEntitiesPossessingComponent(Active.class));

		if(entityManager.gc(current_actor, Action_Component.class).getAction() == null)
			return current_actor;

		entityManager.gc(current_actor, Energy.class).energy += entityManager.gc(current_actor, Energy.class).speed;

		if(entityManager.gc(current_actor, Energy.class).energy < entityManager.gc(current_actor, Action_Component.class).getAction().cost)
			return (current_actor + 1) % actors.size();

		if(entityManager.gc(current_actor, Action_Component.class).getAction().perform())
			return (current_actor + 1) % actors.size();

		entityManager.gc(current_actor, Energy.class).energy -= entityManager.gc(current_actor, Energy.class).speed;
		return current_actor;
		
	}
}
