package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Effects.Damage;

import java.util.ArrayList;

public class OffensiveComponent implements Component{

	public ArrayList<Damage> damages;

	public OffensiveComponent(JSONObject object){

		damages = new ArrayList<>();

		for(Object o : object.keySet()){
			damages.add(new Damage((JSONObject)object.get(o.toString())));
		}
	}

}
