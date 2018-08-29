package roguelike.Effects;

import org.json.simple.JSONObject;

public class Poison extends Effect{
	public Damage damagePerTurn;

	public Poison(JSONObject object) {
		super(object);
		this.damagePerTurn = new Damage((JSONObject)object.get("damage per turn"));
	}

	@Override
	public void update() {

	}

	@Override
	public String verb() {
		return this.verb;
	}
}
