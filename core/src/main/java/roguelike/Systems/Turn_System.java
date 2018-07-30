package roguelike.Systems;

import roguelike.Actions.Action;
import roguelike.Components.Action_Component;
import roguelike.Components.Active;
import roguelike.Components.Command;
import roguelike.Components.Energy;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class Turn_System implements Base_System {

	private SparseLayers display;
	private Energy_System energy_system;
	private AI_System AI_System;

	public Turn_System(SparseLayers display){
		this.display = display;
		this.energy_system = new Energy_System();
	}

	@Override
	public void process() {
		ArrayList<Integer> actors = new ArrayList<>(entityManager.getAllEntitiesPossessingComponent(Active.class));
		actors.sort((a, b) -> a < b ? -1 : a.equals(b) ? 0 : 1);
		int current_actor = actors.get(0);

		this.AI_System = new AI_System(actors, display);

		while(true){

			AI_System.process();
			Command command = entityManager.gc(current_actor, Command.class);
			if(command != null && !display.hasActiveAnimations() && command.hasNext())
			{
				command.next();
			}
			Action action = entityManager.gc(current_actor, Action_Component.class).getAction();

			if(action != null) {

				entityManager.gc(current_actor, Energy.class).energy += entityManager.gc(current_actor, Energy.class).speed;

				if (entityManager.gc(current_actor, Action_Component.class).getAction().perform()) {
					current_actor = actors.get((actors.indexOf(current_actor) + 1) % actors.size());
				} else {
					entityManager.gc(current_actor, Energy.class).energy -= entityManager.gc(current_actor, Energy.class).speed;
				}
			}
			else{
				break;
			}
		}

	}
}
