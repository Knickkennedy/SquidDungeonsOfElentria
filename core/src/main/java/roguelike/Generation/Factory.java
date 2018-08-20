package roguelike.Generation;

import com.badlogic.gdx.Gdx;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import roguelike.Actions.Animations.Animation;
import roguelike.Components.*;
import roguelike.Enums.EquipmentSlot;
import roguelike.Enums.Race;
import roguelike.engine.Game;
import roguelike.utilities.Roll;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;

import java.util.List;
import java.util.PriorityQueue;

import static roguelike.Generation.World.entityManager;

public class Factory {

	public static Factory factory;

	public Game game;
	private SparseLayers display;
	private World world;
	private List<Animation> animations;

	private JSONObject races;
	private JSONObject items;
	private JSONObject item_groups;
	private JSONObject entity_modifiers;
	private JSONObject entities;
	private JSONObject entity_groups;

	public PriorityQueue<Integer> death_queue;

	private Factory() {
		JSONParser parser = new JSONParser();
		death_queue = new PriorityQueue<>();
		try {
			races = (JSONObject) parser.parse(Gdx.files.internal("races.json").reader());
			items = (JSONObject) parser.parse(Gdx.files.internal("items.json").reader());
			item_groups = (JSONObject) parser.parse(Gdx.files.internal("item_groups.json").reader());
			entity_modifiers = (JSONObject) parser.parse(Gdx.files.internal("entity_modifiers.json").reader());
			entities = (JSONObject)parser.parse(Gdx.files.internal("entities.json").reader());
			entity_groups = (JSONObject)parser.parse(Gdx.files.internal("entity_groups.json").reader());
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
	public void setDisplay(SparseLayers display){ this.display = display; }
	public void setWorld(World world){ this.world = world; }
	public void setAnimations(List<Animation> animations){ this.animations = animations; }

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
		entityManager.gc(player, Details.class).race = Race.PLAYER;
		entityManager.addComponent(player, new Active());
		entityManager.addComponent(player, new ActionComponent());
		entityManager.addComponent(player, new Energy(100));
		entityManager.addComponent(player, initializeCommandDisplay(player, display, world, animations));
		entityManager.addComponent(player, new Inventory());
		entityManager.addComponent(player, new Equipment());
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("iron breastplate"), EquipmentSlot.CHEST);
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("iron helm"), EquipmentSlot.HEAD);
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("iron shortsword"), EquipmentSlot.LEFT_HAND);
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("shortbow"), EquipmentSlot.RANGED_WEAPON);
		entityManager.gc(player, Equipment.class).equip_item(player, create_new_item("iron arrow"), EquipmentSlot.AMMUNITION);



		for(int i = 0; i < 5; i++){
			Integer new_enemy = create_new_entity("group:rockthrowers");
			entityManager.gc(new_enemy, Position.class).map = current_map;
			entityManager.gc(new_enemy, Position.class).location = Coord.get(20 + i + 1, 20 );
			entityManager.addComponent(new_enemy, new Vision(Coord.get(20 + i + 1, 20), current_map, 5.0));
			current_map.entities.add(new_enemy);
		}

		current_map.entities.add(player);
	}

	public Command initializeCommandDisplay(Integer entity, SparseLayers display, World world, List<Animation> animations){
		return new Command(entity, game, display, world, animations);
	}

	public Integer create_new_entity(String name){
		Integer entity = entityManager.createEntity();

		JSONObject entity_type;
		JSONObject base_entity = new JSONObject();
		JSONArray additions = new JSONArray();

		if(name.contains("group")){
			String[] split = name.split(":");
			JSONArray entity_array = (JSONArray) entity_groups.get(split[1]);
			entity_type = (JSONObject) entities.get(entity_array.get(Roll.rand(0, entity_array.size() - 1)));
		}
		else {
			entity_type = (JSONObject) entities.get(name);
		}

		for(Object o : entity_type.keySet()){
			switch (o.toString()){
				case "base":        base_entity = (JSONObject)races.get(entity_type.get("base")); break;
				case "modifier":    additions = (JSONArray)(entity_type.get("modifier")); break;
			}
		}

		entityManager.addComponent(entity, new Position());
		entityManager.addComponent(entity, new Inventory());
		entityManager.addComponent(entity, new ActionComponent());
		entityManager.addComponent(entity, new Active());

		for(Object o : base_entity.keySet()){
			switch (o.toString()){
				case "sprite": entityManager.addComponent(entity, new Sprite((JSONObject)base_entity.get(o.toString()))); break;
				case "statistics": entityManager.addComponent(entity, new Statistics((JSONObject)base_entity.get(o.toString()))); break;
				case "details": entityManager.addComponent(entity, new Details((JSONObject)base_entity.get(o.toString()))); break;
				case "speed": entityManager.addComponent(entity, new Energy((int)(long)base_entity.get(o.toString()))); break;
				case "ai": entityManager.addComponent(entity, new AI()); break;
				case "equipment": entityManager.addComponent(entity, new Equipment((JSONObject)base_entity.get(o.toString()))); break;
			}
		}

		for (Object addition : additions) {
			JSONObject temp = (JSONObject) entity_modifiers.get(addition);
			for (Object o : temp.keySet()) {
				switch (o.toString()) {
					case "statistics":
						entityManager.gc(entity, Statistics.class).update_base_stats((JSONObject) temp.get(o.toString()));
						break;
					case "name modifier":
						entityManager.gc(entity, Details.class).update_name((JSONObject) temp.get(o.toString()));
						break;
				}
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
					entityManager.addComponent(item, new OffensiveComponent((JSONObject)item_properties.get(o))); break;
				case "range":
					entityManager.addComponent(item, new Range((int)(long)item_properties.get(o))); break;
				case "ranged modifiers":
					entityManager.addComponent(item, new RangedModifiers((JSONObject)item_properties.get(o))); break;
			}
		}

		return item;
	}
}
