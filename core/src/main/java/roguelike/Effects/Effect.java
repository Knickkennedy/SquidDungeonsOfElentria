package roguelike.Effects;

import org.json.simple.JSONObject;

public abstract class Effect {
	public int duration;
	public String verb;
	public abstract void update();
	public abstract String verb();

	public Effect(JSONObject object){
		for(Object o : object.keySet()){
			switch (o.toString()){
				case "verb": this.verb = (String)object.get(o.toString()); break;
				case "duration": this.duration = (int)(long)object.get(o.toString()); break;
			}
		}
	}
}
