package roguelike.Systems;

import roguelike.Actions.Move;
import roguelike.Components.AI;
import roguelike.Components.Action_Component;
import roguelike.Components.Active;
import roguelike.Components.Energy;
import roguelike.utilities.Point;

import java.util.ArrayList;
import java.util.Set;

import static roguelike.Generation.World.entityManager;

public class Turn_System implements System {

	private Energy_System energy_system;
	private AI_System AI_system;

	public Turn_System(){
		this.energy_system = new Energy_System();
		this.AI_system = new AI_System();
	}

	@Override
	public int process(int current_actor) {
		ArrayList<Integer> actors = new ArrayList<>(entityManager.getAllEntitiesPossessingComponent(Active.class));

		int index = actors.indexOf(current_actor);

		energy_system.process(current_actor);
		AI_system.process(current_actor);

		if(entityManager.gc(current_actor, Energy.class).energy < entityManager.gc(current_actor, Action_Component.class).getAction().cost)
			return actors.get((index + 1) % actors.size());

		if(entityManager.gc(current_actor, Action_Component.class).getAction().perform())
			return actors.get((index + 1) % actors.size());

		entityManager.gc(current_actor, Energy.class).energy -= entityManager.gc(current_actor, Energy.class).speed;
		return actors.get(index % actors.size());

	}
}
