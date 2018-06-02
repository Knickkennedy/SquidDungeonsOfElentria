package roguelike.Generation;

import com.badlogic.gdx.Gdx;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import roguelike.Components.*;
import roguelike.Systems.Turn_System;
import roguelike.engine.EntityManager;
import roguelike.screens.Game_Screen;
import roguelike.utilities.Point;

import java.io.IOException;
import java.util.Scanner;

@Getter
@Setter
public class World {

	public static EntityManager entityManager = new EntityManager();

	private int map_width;
	private int map_height;

	private JSONObject tiles;
	private JSONObject races;
	private JSONObject items;

	private Map surface;
	private Map current_map;

	private Integer player;
	private Point starting_location;

	private Integer current_actor;
	private Turn_System turn_system;

	public World(int map_width, int map_height) throws IOException, ParseException {
		this.map_width = map_width;
		this.map_height = map_height;

		JSONParser parser = new JSONParser();
		tiles = (JSONObject)parser.parse(Gdx.files.internal("tiles.txt").reader());
		races = (JSONObject)parser.parse(Gdx.files.internal("races.txt").reader());
		items = (JSONObject)parser.parse(Gdx.files.internal("items.txt").reader());
		surface = new Map(initializeMapWithFile("/surface.txt"));
		current_map = surface;
		initializePlayer();
		Gdx.input.setInputProcessor(entityManager.gc(player, Command.class));
		turn_system = new Turn_System();
		current_actor = player;
	}

	public void initializePlayer(){
		player = entityManager.createEntity();
		entityManager.addComponent(player, new Position(starting_location, current_map));
		entityManager.addComponent(player, new Vision(starting_location, current_map, 5.0));
		JSONObject human = (JSONObject) races.get("human");
		entityManager.addComponent(player, new Sprite((JSONObject)human.get("sprite")));
		entityManager.addComponent(player, new Active());
		entityManager.addComponent(player, new Action_Component());
		entityManager.addComponent(player, new Energy(100));
		entityManager.addComponent(player, new Command(player));
		entityManager.addComponent(player, new Inventory());
		entityManager.gc(player, Inventory.class).add_item(create_new_item("leather armor"));
		entityManager.gc(player, Inventory.class).add_item(create_new_item("iron helm"));
		for(int i = 0; i < entityManager.gc(player, Inventory.class).inventory.size(); i++){
			System.out.println(entityManager.gc(entityManager.gc(player, Inventory.class).inventory.get(i), Details.class).name + " "
					+ entityManager.gc(entityManager.gc(player, Inventory.class).inventory.get(i), Details.class).description + " "
					+ entityManager.gc(entityManager.gc(player, Inventory.class).inventory.get(i), Equippable.class).slots.get(0));
		}
	}

	public Integer create_new_item(String name){
		Integer item = entityManager.createEntity();

		JSONObject item_properties = (JSONObject)items.get(name);

		for(Object o : item_properties.keySet()) {
			switch (o.toString()) {
				case "details":
					entityManager.addComponent(item, new Details(name, (JSONObject) item_properties.get(o)));
					break;
				case "equippable":
					entityManager.addComponent(item, new Equippable((JSONObject)item_properties.get(o)));
					break;
			}
		}

		return item;
	}

	private Tile[][] initializeMapWithFile(String fileName){

		Scanner fileScanner = openFile(fileName);

		String line;
		Tile[][] mapToReturn = new Tile[map_width][map_height];

		int index = 0;

		while(fileScanner.hasNextLine()){
			line = fileScanner.nextLine();
			JSONObject tile;
			for(int i = 0; i < line.length(); i++){
				char c = line.charAt(i);

				if(c == '='){
					tile = (JSONObject) tiles.get("water");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '^'){
					tile = (JSONObject) tiles.get("mountain");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '"'){
					tile = (JSONObject) tiles.get("grass");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '&'){
					tile = (JSONObject) tiles.get("forest");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '.'){
					tile = (JSONObject) tiles.get("road");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '*'){
					tile = (JSONObject) tiles.get("cave");
					mapToReturn[i][index] = new Tile(tile);
					//main_entrance = new Point(i, index);
				}
				else if(c == 'X') {
					tile = (JSONObject) tiles.get("road");
					mapToReturn[i][index] = new Tile(tile);
					starting_location = new Point(i, index);
				}
			}
			index++;
		}

		fileScanner.close();

		return mapToReturn;
	}

	public void update(){
		while(entityManager.gc(current_actor, Action_Component.class).getAction() != null) {
			current_actor = turn_system.process(current_actor);
		}
	}

	private static Scanner openFile(String fileName) {
		return new Scanner(Game_Screen.class.getResourceAsStream(fileName));
	}
}
