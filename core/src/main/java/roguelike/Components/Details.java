package roguelike.Components;

import org.json.simple.JSONObject;

public class Details implements Component{
	public String name;
	public String description;

	public boolean isPlayer;

	public Details(JSONObject object){

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "name": name = (String)object.get(o.toString()); break;
				case "description": description = (String)object.get(o.toString()); break;
			}
		}

		isPlayer = false;
	}

	public String getName(){
		return name != null ? name : "";
	}
}
