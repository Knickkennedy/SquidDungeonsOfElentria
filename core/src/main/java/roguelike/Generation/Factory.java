package roguelike.Generation;

import com.badlogic.gdx.Gdx;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import roguelike.Components.*;
import roguelike.Enums.Equipment_Slot;
import roguelike.utilities.Point;

import java.io.IOException;

import static roguelike.Generation.World.entityManager;

public class Factory {

	private JSONObject races;
	private JSONObject items;

	public Factory() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		races = (JSONObject)parser.parse(Gdx.files.internal("races.txt").reader());
		items = (JSONObject)parser.parse(Gdx.files.internal("items.txt").reader());
	}

	public Integer initialize_player(){
		return entityManager.createEntity();
	}

	public void build_player(Integer player, Point starting_location, Map current_map){
		entityManager.addComponent(player, new Position(current_map));
		entityManager.gc(player, Position.class).location = starting_location;
		entityManager.addComponent(player, new Vision(starting_location, current_map, 5.0));
		JSONObject human = (JSONObject) races.get("human");
		entityManager.addComponent(player, new Sprite((JSONObject)human.get("sprite")));
		entityManager.addComponent(player, new Statistics((JSONObject)human.get("statistics")));
		entityManager.addComponent(player, new Active());
		entityManager.addComponent(player, new Action_Component());
		entityManager.addComponent(player, new Energy(100));
		entityManager.addComponent(player, new Command(player));
		entityManager.addComponent(player, new Inventory());
		entityManager.addComponent(player, new Equipment());
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("leather armor"), Equipment_Slot.CHEST);
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("iron helm"), Equipment_Slot.HEAD);
	}

	public Integer create_new_item(String name){
		Integer item = entityManager.createEntity();

		JSONObject item_properties = (JSONObject)items.get(name);

		for(Object o : item_properties.keySet()) {
			switch (o.toString()) {
				case "details":
					entityManager.addComponent(item, new Details(name, (JSONObject) item_properties.get(o))); break;
				case "equippable":
					entityManager.addComponent(item, new Equippable((JSONObject)item_properties.get(o))); break;
				case "defenses":
					entityManager.addComponent(item, new Armor((JSONObject)item_properties.get(o))); break;
			}
		}

		return item;
	}
}
