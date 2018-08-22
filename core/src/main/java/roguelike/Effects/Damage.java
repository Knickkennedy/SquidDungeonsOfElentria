package roguelike.Effects;

import org.json.simple.JSONObject;
import roguelike.utilities.Dice;

public class Damage {
	public String type;
	public Dice dice;

	public Damage(JSONObject object){
		for(Object o : object.keySet()){
			switch (o.toString()){
				case "type": type = (String)object.get(o.toString()); break;
				case "dice": dice = new Dice((JSONObject)object.get(o.toString())); break;
			}
		}
	}

	public Damage(String type, Dice dice){
		this.type = type;
		this.dice = dice;
	}

	public int roll(){
		return dice.roll();
	}

	@Override
	public String toString(){
		return String.format("Type: %s, Dice Rolled: %s", type, dice);
	}
}
