package roguelike.Systems;

import roguelike.Components.Action_Component;
import roguelike.Components.Active;
import roguelike.Components.Energy;

import java.util.ArrayList;
import java.util.Collections;

import static roguelike.Generation.World.entityManager;

public class Turn_System implements Base_System {

	private Energy_System energy_system;
	private AI_System AI_System;

	public Turn_System(){
		this.energy_system = new Energy_System();
	}

	@Override
	public void process() {
		ArrayList<Integer> actors = new ArrayList<>(entityManager.getAllEntitiesPossessingComponent(Active.class));
		Collections.sort(actors, (a, b) -> a < b ? -1 : a.equals(b) ? 0 : 1);
		int current_actor = actors.get(0);

		this.AI_System = new AI_System(actors);
		AI_System.process();

		while (entityManager.gc(current_actor, Action_Component.class).getAction() != null){

			entityManager.gc(current_actor, Energy.class).energy += entityManager.gc(current_actor, Energy.class).speed;

			if(entityManager.gc(current_actor, Action_Component.class).getAction().perform()){
				current_actor = actors.get((actors.indexOf(current_actor) + 1) % actors.size());
			}
			else{
				entityManager.gc(current_actor, Energy.class).energy -= entityManager.gc(current_actor, Energy.class).speed;
			}
		}

	}
}
