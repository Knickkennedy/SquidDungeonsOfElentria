package roguelike.Effects;

import org.json.simple.JSONObject;

public abstract class Effect {
	public int duration;
	public String updateMessage;
	public boolean isFinished;
	public abstract void update(Integer actor);
	public abstract void checkIfFinished();

	public Effect(JSONObject object){
		for(Object o : object.keySet()){
			switch (o.toString()){
				case "update message": this.updateMessage = (String)object.get(o.toString()); break;
				case "duration": this.duration = (int)(long)object.get(o.toString()); break;
			}
		}

		isFinished = false;
	}
}
