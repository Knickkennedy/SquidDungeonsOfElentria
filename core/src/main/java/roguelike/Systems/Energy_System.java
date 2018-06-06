package roguelike.Systems;

import roguelike.Components.Energy;

import static roguelike.Generation.World.entityManager;

public class Energy_System implements System{


	@Override
	public int process(int current_actor) {

		entityManager.gc(current_actor, Energy.class).energy += entityManager.gc(current_actor, Energy.class).speed;

		return 0;
	}
}
