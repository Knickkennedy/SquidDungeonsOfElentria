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

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@Getter
@Setter
public class World {

	public static EntityManager entityManager = new EntityManager();

	private int map_width;
	private int map_height;

	private JSONObject tile_file;
	private JSONObject race_file;

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
		tile_file = (JSONObject)parser.parse(Gdx.files.internal("tiles.txt").reader());
		race_file = (JSONObject)parser.parse(Gdx.files.internal("races.txt").reader());
		surface = new Map(initializeMapWithFile("/surface.txt"));
		current_map = surface;
		player = entityManager.createEntity();
		entityManager.addComponent(player, new Position(starting_location, current_map));
		JSONObject human = (JSONObject)race_file.get("human");
		entityManager.addComponent(player, new Sprite((JSONObject)human.get("sprite")));
		entityManager.addComponent(player, new Active());
		entityManager.addComponent(player, new Action_Component());
		entityManager.addComponent(player, new Energy(100));
		entityManager.addComponent(player, new Command(player));
		Gdx.input.setInputProcessor(entityManager.gc(player, Command.class));
		turn_system = new Turn_System();
		current_actor = 0;
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
					tile = (JSONObject)tile_file.get("water");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '^'){
					tile = (JSONObject)tile_file.get("mountain");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '"'){
					tile = (JSONObject)tile_file.get("grass");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '&'){
					tile = (JSONObject)tile_file.get("forest");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '.'){
					tile = (JSONObject)tile_file.get("road");
					mapToReturn[i][index] = new Tile(tile);
				}
				else if(c == '*'){
					tile = (JSONObject)tile_file.get("cave");
					mapToReturn[i][index] = new Tile(tile);
					//main_entrance = new Point(i, index);
				}
				else if(c == 'X') {
					tile = (JSONObject)tile_file.get("road");
					mapToReturn[i][index] = new Tile(tile);
					starting_location = new Point(i, index);
				}
			}
			index++;
		}

		return mapToReturn;
	}

	public void update(){
		current_actor = turn_system.process(current_actor);

		/*if(entityManager.gc(player, Command.class).getInput().hasNext()){
			entityManager.gc(player, Command.class).getInput().next();
		}*/
	}

	private static Scanner openFile(String fileName) {
		return new Scanner(Game_Screen.class.getResourceAsStream(fileName));
	}
}
