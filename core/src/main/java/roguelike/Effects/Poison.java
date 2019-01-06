package roguelike.Effects;

import org.json.simple.JSONObject;
import roguelike.Components.Statistics;
import roguelike.Generation.Factory;
import roguelike.engine.MessageLog;

import static roguelike.Generation.World.entityManager;

public class Poison extends Effect{
	public Damage damagePerTurn;

	public Poison(JSONObject object) {
		super(object);
		this.damagePerTurn = new Damage((JSONObject)object.get("damage per turn"));
	}

	@Override
	public void update(Integer actor) {
		entityManager.gc(actor, Statistics.class).get_stat("health").changeValue(-damagePerTurn.roll());
		duration--;

		MessageLog.getInstance().addEffectMessage(updateMessage, actor);

		checkIfFinished();

		if(entityManager.gc(actor, Statistics.class).get_stat("health").isMinimum() && !Factory.getInstance().death_queue.contains(actor)){
			Factory.getInstance().death_queue.add(actor);
		}
	}

	@Override
	public void checkIfFinished() {
		if(duration <= 0)
			isFinished = true;
	}
}
