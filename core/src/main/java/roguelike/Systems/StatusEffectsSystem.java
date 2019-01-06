package roguelike.Systems;

import roguelike.Components.StatusEffects;
import roguelike.Effects.Effect;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class StatusEffectsSystem implements BaseSystem{
	@Override
	public void process() {

	}

	@Override
	public void process(Integer actor) {
		StatusEffects statusEffects = entityManager.gc(actor, StatusEffects.class);
		ArrayList<Effect> finishedEffects = new ArrayList<>();
		for(Effect effect : statusEffects.statusEffects){
			effect.update(actor);
			if(effect.isFinished)
				finishedEffects.add(effect);
		}

		statusEffects.statusEffects.removeAll(finishedEffects);
	}
}
