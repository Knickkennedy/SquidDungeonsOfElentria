package roguelike.Components;

import org.json.simple.JSONObject;

public class Armor implements Component{

	public int piercing;
	public int slashing;
	public int crushing;

	public Armor(JSONObject object){

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "piercing": piercing = (int)(long)object.get(o.toString()); break;
				case "slashing": slashing = (int)(long)object.get(o.toString()); break;
				case "crushing": crushing = (int)(long)object.get(o.toString()); break;
			}
		}
	}

	@Override
	public String toString(){
		return String.format("Piercing: %d, Slashing: %d, Crushing: %d", piercing, slashing, crushing);
	}

}
