package roguelike.Systems;

import roguelike.Actions.Action;
import roguelike.Actions.Animations.Animation;
import roguelike.Components.ActionComponent;
import roguelike.Components.Active;
import roguelike.Components.Command;
import roguelike.Components.Energy;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import java.util.ArrayList;
import java.util.List;

import static roguelike.Generation.World.entityManager;

public class TurnSystem implements BaseSystem {

	private SparseLayers display;
	private List<Animation>animations;
	private EnergySystem energy_system;
	private AISystem AISystem;

	public TurnSystem(SparseLayers display, List<Animation> animations){
		this.display = display;
		this.animations = animations;
		this.energy_system = new EnergySystem();
	}

	@Override
	public void process() {
		ArrayList<Integer> actors = new ArrayList<>(entityManager.getAllEntitiesPossessingComponent(Active.class));
		actors.sort((a, b) -> a < b ? -1 : a.equals(b) ? 0 : 1);
		int current_actor = actors.get(0);

		this.AISystem = new AISystem(actors, display, animations);

		while(true){

			Active active = entityManager.gc(current_actor, Active.class);
			if(active != null) {
				AISystem.process(current_actor);
				Command command = entityManager.gc(current_actor, Command.class);

				if (command != null && !display.hasActiveAnimations() && command.hasNext()) {
					command.next();
				}
				Action action = entityManager.gc(current_actor, ActionComponent.class).getAction();

				if (action != null) {

					Energy energy = entityManager.gc(current_actor, Energy.class);
					energy.energy += energy.speed;

					while (true){
						if(action.isAlternativeAction()) {
							action = entityManager.gc(current_actor, ActionComponent.class).getAction();
							if(action == null)
								break;
						}
						else
							break;
					}

					if (action != null && action.canPerform()) {
						action.perform();
						current_actor = actors.get((actors.indexOf(current_actor) + 1) % actors.size());
					}
				}
				else
					break;
			}
			else
				break;
		}

	}

	@Override
	public void process(Integer actor) {

	}
}
