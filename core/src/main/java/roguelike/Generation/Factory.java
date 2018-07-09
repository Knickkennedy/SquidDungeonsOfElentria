package roguelike.Generation;

import com.badlogic.gdx.Gdx;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import roguelike.Components.*;
import roguelike.Enums.Equipment_Slot;
import roguelike.engine.Game;
import roguelike.utilities.Roll;
import squidpony.squidmath.Coord;

import static roguelike.Generation.World.entityManager;

public class Factory {

	public static Factory factory;

	public Game game;

	private JSONObject races;
	private JSONObject items;
	private JSONObject item_groups;

	private Factory() {
		JSONParser parser = new JSONParser();
		try {
			races = (JSONObject) parser.parse(Gdx.files.internal("races.json").reader());
			items = (JSONObject) parser.parse(Gdx.files.internal("items.json").reader());
			item_groups = (JSONObject) parser.parse(Gdx.files.internal("item_groups.json").reader());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Factory getInstance() {
		if(factory == null)
			factory = new Factory();

		return factory;
	}

	public void setGame(Game game){
		this.game = game;
	}

	public Integer initialize_player(){
		return entityManager.createEntity();
	}

	public void build_player(Integer player, Coord starting_location, Map current_map){
		entityManager.addComponent(player, new Position(current_map));
		entityManager.gc(player, Position.class).location = starting_location;
		entityManager.addComponent(player, new Vision(starting_location, current_map, 5.0));
		JSONObject human = (JSONObject) races.get("human");
		entityManager.addComponent(player, new Sprite((JSONObject)human.get("sprite")));
		entityManager.addComponent(player, new Statistics((JSONObject)human.get("statistics")));
		entityManager.addComponent(player, new Details((JSONObject)human.get("details")));
		entityManager.gc(player, Details.class).isPlayer = true;
		entityManager.addComponent(player, new Active());
		entityManager.addComponent(player, new Action_Component());
		entityManager.addComponent(player, new Energy(100));
		entityManager.addComponent(player, new Command(player, game));
		entityManager.addComponent(player, new Inventory());
		entityManager.addComponent(player, new Equipment());
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("leather armor"), Equipment_Slot.CHEST);
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("iron helm"), Equipment_Slot.HEAD);
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("iron shortsword"), Equipment_Slot.LEFT_HAND);



		for(int i = 0; i < 5; i++){
			Integer new_enemy = create_new_entity("goblin");
			entityManager.gc(new_enemy, Position.class).map = current_map;
			entityManager.gc(new_enemy, Position.class).location = Coord.get(20 + i + 1, 20 );
			entityManager.addComponent(new_enemy, new Vision(Coord.get(20 + i + 1, 20), current_map, 5.0));
			current_map.entities.add(new_enemy);
		}





		current_map.entities.add(player);
	}

	public Integer create_new_entity(String name){
		Integer entity = entityManager.createEntity();

		JSONObject entity_properties = (JSONObject)races.get(name);

		entityManager.addComponent(entity, new Position());
		entityManager.addComponent(entity, new Inventory());
		entityManager.addComponent(entity, new Action_Component());
		entityManager.addComponent(entity, new Active());

		for(Object o : entity_properties.keySet()){

			switch (o.toString()){
				case "sprite": entityManager.addComponent(entity, new Sprite((JSONObject)entity_properties.get(o.toString()))); break;
				case "statistics": entityManager.addComponent(entity, new Statistics((JSONObject)entity_properties.get(o.toString()))); break;
				case "details": entityManager.addComponent(entity, new Details((JSONObject)entity_properties.get(o.toString()))); break;
				case "speed": entityManager.addComponent(entity, new Energy((int)(long)entity_properties.get(o.toString()))); break;
				case "ai": entityManager.addComponent(entity, new AI()); break;
				case "equipment": entityManager.addComponent(entity, new Equipment((JSONObject)entity_properties.get(o.toString()))); break;
			}
		}

		return entity;
	}

	public Integer create_new_item(String name){
		Integer item = entityManager.createEntity();

		JSONObject item_properties;

		if(name.contains("group")){
			String[] split = name.split(":");
			JSONArray item_array = (JSONArray) item_groups.get(split[1]);
			item_properties = (JSONObject) items.get(item_array.get(Roll.rand(0, item_array.size() - 1)));
		}
		else {
			item_properties = (JSONObject) items.get(name);
		}


		for(Object o : item_properties.keySet()) {
			switch (o.toString()) {
				case "details":
					entityManager.addComponent(item, new Details((JSONObject)item_properties.get(o))); break;
				case "equippable":
					entityManager.addComponent(item, new Equippable((JSONObject)item_properties.get(o))); break;
				case "defenses":
					entityManager.addComponent(item, new Armor((JSONObject)item_properties.get(o))); break;
				case "attack":
					entityManager.addComponent(item, new Offensive_Component((JSONObject)item_properties.get(o))); break;
			}
		}

		return item;
	}
}
