package roguelike.Generation;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import roguelike.Components.Sprite;

@Getter @Setter
public class Tile {
	private String name;
	private Sprite sprite;
	private String description;
	private int movement_cost;
	private boolean passable;

	public Tile(JSONObject tile){
		for(Object o : tile.keySet()){
			switch (o.toString()){
				case "sprite": 			sprite = new Sprite((JSONObject)tile.get(o)); break;
				case "description": 	description = (String)tile.get(o); break;
				case "movement cost":	movement_cost = (int)(long)tile.get(o); break;
				case "passable": 		passable = (boolean)tile.get(o); break;
			}
		}
	}
}
