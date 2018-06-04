package roguelike.Generation;

import org.json.simple.JSONObject;
import roguelike.Components.Sprite;

public class Tile {
	public String name;
	public Sprite sprite;
	public String description;
	public Exit exit;
	public int movement_cost;
	public boolean passable;
	public boolean openable;

	public Tile(JSONObject tile){

		openable = false;

		for(Object o : tile.keySet()){
			switch (o.toString()){
				case "sprite": 			sprite = new Sprite((JSONObject)tile.get(o)); break;
				case "description": 	description = (String)tile.get(o); break;
				case "movement cost":	movement_cost = (int)(long)tile.get(o); break;
				case "passable": 		passable = (boolean)tile.get(o); break;
				case "openable":        openable = (boolean)tile.get(o); break;
			}
		}
	}
}
