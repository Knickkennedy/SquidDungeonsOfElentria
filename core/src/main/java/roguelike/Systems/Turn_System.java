package roguelike.Systems;

import roguelike.Components.Action_Component;
import roguelike.Components.Active;
import roguelike.Components.Energy;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class Turn_System implements Base_System {

	private Energy_System energy_system;

	public Turn_System(){
		this.energy_system = new Energy_System();
	}

	@Override
	public int process(int current_actor) {
		ArrayList<Integer> actors = new ArrayList<>(entityManager.getAllEntitiesPossessingComponent(Active.class));

		int index = actors.indexOf(current_actor);

		energy_system.process(current_actor);

		/*if(entityManager.gc(current_actor, Energy.class).energy < entityManager.gc(current_actor, Action_Component.class).getAction().cost)
			return actors.get((index + 1) % actors.size());*/

		if(entityManager.gc(current_actor, Action_Component.class).getAction().perform())
			return actors.get((index + 1) % actors.size());

		entityManager.gc(current_actor, Energy.class).energy -= entityManager.gc(current_actor, Energy.class).speed;
		return current_actor;

	}
}
