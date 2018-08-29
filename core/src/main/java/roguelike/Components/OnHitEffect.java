package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Effects.Effect;
import roguelike.Effects.Poison;

import static roguelike.Generation.World.entityManager;

public class OnHitEffect {
	public Effect onHitEffect;

	public OnHitEffect(JSONObject object){
		for(Object o : object.keySet()) {
			switch (o.toString()) {
				case "poison":
					onHitEffect = new Poison(object);
					break;
			}
		}
	}

	public void apply(Integer target){
		entityManager.gc(target, StatusEffects.class).add(onHitEffect);
	}
}
