package roguelike.Systems;

import roguelike.Components.Energy;

import static roguelike.Generation.World.entityManager;

public class EnergySystem implements BaseSystem {

	@Override
	public void process() {
	}

	@Override
	public void process(Integer actor) {
		Energy energy = entityManager.gc(actor, Energy.class);
		energy.energy += energy.speed;
	}
}
