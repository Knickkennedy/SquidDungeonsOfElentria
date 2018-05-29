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

		Action action = entityManager.gc(actors.get(current_actor), Action_Component.class).getAction();

		if(action == null) return current_actor;

		entityManager.gc(actors.get(current_actor), Energy.class).energy += entityManager.gc(actors.get(current_actor), Energy.class).speed;

			if(!action.perform()){
				entityManager.gc(actors.get(current_actor), Energy.class).energy -= entityManager.gc(actors.get(current_actor), Energy.class).speed;
				return current_actor;
			} else{
				return (current_actor + 1) % actors.size();
			}
	}
}
