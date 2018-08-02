package roguelike.Systems;

import roguelike.Actions.Action;
import roguelike.Components.ActionComponent;
import roguelike.Components.Active;
import roguelike.Components.Command;
import roguelike.Components.Energy;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class TurnSystem implements BaseSystem {

	private SparseLayers display;
	private EnergySystem energy_system;
	private AISystem AISystem;

	public TurnSystem(SparseLayers display){
		this.display = display;
		this.energy_system = new EnergySystem();
	}

	@Override
	public void process() {
		ArrayList<Integer> actors = new ArrayList<>(entityManager.getAllEntitiesPossessingComponent(Active.class));
		actors.sort((a, b) -> a < b ? -1 : a.equals(b) ? 0 : 1);
		int current_actor = actors.get(0);

		this.AISystem = new AISystem(actors, display);

		while(true){

			Active active = entityManager.gc(current_actor, Active.class);
			if(active != null) {
				AISystem.process();
				Command command = entityManager.gc(current_actor, Command.class);

				if (command != null && !display.hasActiveAnimations() && command.hasNext()) {
					command.next();
				}
				Action action = entityManager.gc(current_actor, ActionComponent.class).getAction();

				if (action != null) {

					entityManager.gc(current_actor, Energy.class).energy += entityManager.gc(current_actor, Energy.class).speed;

					if (entityManager.gc(current_actor, ActionComponent.class).getAction().perform()) {
						current_actor = actors.get((actors.indexOf(current_actor) + 1) % actors.size());
					}
					else {
						entityManager.gc(current_actor, Energy.class).energy -= entityManager.gc(current_actor, Energy.class).speed;
					}
				}
				else
					break;
			}
			else
				break;
		}

	}
}
