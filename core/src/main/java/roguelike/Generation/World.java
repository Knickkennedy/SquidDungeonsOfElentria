package roguelike.Generation;

import com.badlogic.gdx.Gdx;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import roguelike.Components.Command;
import roguelike.Components.Position;
import roguelike.Systems.Turn_System;
import roguelike.engine.EntityManager;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.Scanner;

@Getter
@Setter
public class World {

	public static EntityManager entityManager = new EntityManager();

	private int map_width;
	private int map_height;

	private JSONObject tiles;

	private Map surface;
	private Map current_map;
	private Dungeon first_dungeon;

	private Integer player;
	private Coord starting_location;

	public static Coord first_dungeon_location;
	private Turn_System turn_system;

	public ArrayList<Exit> surface_exits;

	public World(int map_width, int map_height) {
		this.map_width = map_width;
		this.map_height = map_height;

		surface_exits = new ArrayList<>();

		JSONParser parser = new JSONParser();
		try {
			tiles = (JSONObject)parser.parse(Gdx.files.internal("tiles.json").reader());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		first_dungeon = new Dungeon("Main Dungeon", 25);
		surface = new Map(initializeMapWithFile("surface.txt"));
		first_dungeon.add_level(0, surface);
		first_dungeon.build_basic_dungeon();
		initialize_exits();

		current_map = surface;
		player = Factory.getInstance().initialize_player();
		Factory.getInstance().build_player(player, starting_location, surface);
		turn_system = new Turn_System();
		reload();
	}
	public void reload()
	{
		Gdx.input.setInputProcessor(entityManager.gc(player, Command.class));
	}

	public void initialize_exits(){
		surface.exits.addAll(surface_exits);
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
				else if(c == '1'){
					tile = (JSONObject) tiles.get("cave");
					mapToReturn[i][index] = new Tile(tile);
					surface_exits.add(new Exit(first_dungeon, Coord.get(i, index), 1, "stairs - up"));
					first_dungeon_location = Coord.get(i, index);
				}
				else if(c == 'X') {
					tile = (JSONObject) tiles.get("road");
					mapToReturn[i][index] = new Tile(tile);
					starting_location = Coord.get(i, index);
				}
			}
			index++;
		}

		fileScanner.close();

		return mapToReturn;
	}

	public void update(){

		turn_system.process();

		current_map = entityManager.gc(player, Position.class).map;
	}

	private static Scanner openFile(String fileName) {
		return new Scanner(Gdx.files.internal(fileName).reader());
	}
}
