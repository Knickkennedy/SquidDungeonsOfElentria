package roguelike.Components;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import roguelike.Enums.Hostility;
import roguelike.Enums.Race;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class Details implements Component{
	public String name;
	public String description;
	public Race race;
	public ArrayList<Hostility> hostile_to;

	public boolean isPlayer;

	public Details(JSONObject object){

		hostile_to = new ArrayList<>();

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "name": name = (String)object.get(o.toString()); break;
				case "description": description = (String)object.get(o.toString()); break;
				case "hostile towards":{
					JSONArray array = (JSONArray)object.get(o.toString());
					for(Object type : array){
						hostile_to.add(Race.get_race((String)type));
					}
					break;
				}
			}
		}

		race = Race.get_race(name);
		isPlayer = false;
	}

	public boolean is_hostile_towards(Integer entity){
		Race entity_race = entityManager.gc(entity, Details.class).race;

		for(Hostility hostility : hostile_to){
			if(entity_race == hostility)
				return true;
		}

		for(Hostility hostility : entityManager.gc(entity, Details.class).hostile_to){
			if(hostility == race)
				return true;
		}

		return false;
	}

	public String getName(){
		return name != null ? name : "";
	}

	public void update_name(JSONObject object){

		boolean isPrefix = (boolean)object.get("prefix");
		if(isPrefix){
			String temp = (String)object.get("modifier");
			name = String.format("%s %s", temp, name);
		}
		else{
			String temp = (String)object.get("modifier");
			name = String.format("%s %s", name, temp);
		}
	}
}
