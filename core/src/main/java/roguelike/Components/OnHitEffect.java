package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Effects.Effect;
import roguelike.Effects.Poison;

import static roguelike.Generation.World.entityManager;

public class OnHitEffect implements Component{
	public JSONObject object;
	public String type;
	public double procChance;

	public OnHitEffect(JSONObject object){
		this.object = object;
		for(Object o : object.keySet()) {
			switch (o.toString()) {
				case "poison":
					type = "poison";
					break;
				case "proc chance":
					procChance = (double)object.get(o.toString());
					break;
			}
		}
	}

	public void apply(Integer target){

		switch (type){
			case "poison":
				entityManager.gc(target, StatusEffects.class).add(new Poison((JSONObject)object.get("poison")));
				break;
		}
	}
}
