package roguelike.Generation;

import com.badlogic.gdx.Gdx;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import roguelike.Components.Position;
import squidpony.squidmath.Coord;

import java.io.IOException;
import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class Map{

    private Tile[][] tiles;
    public char[][] pathfinding;

    private JSONObject tile_file;

    public ArrayList<Exit> exits;

    private MapBuilder builder;
    public Coord stairs_down;
    public Coord stairs_up;

    public ArrayList<Integer> entities;

    public boolean isBuilt;

    public Map(final Tile[][] tiles) {
        this.tiles = tiles;
        this.exits = new ArrayList<>();
        initializePathFinding();
        entities = new ArrayList<>();

	    JSONParser parser = new JSONParser();
	    try {
		    this.tile_file = (JSONObject)parser.parse(Gdx.files.internal("tiles.json").reader());
	    } catch (IOException | ParseException e) {
		    e.printStackTrace();
	    }
	    this.isBuilt = false;
    }

    public Map(final int width, final int height) {
        builder = new MapBuilder(width, height);
        tile_file = builder.tile_file;
        this.isBuilt = false;
	    entities = new ArrayList<>();
    }

    public Tile getTileAt(int x, int y){
        return tiles[x][y];
    }

    public void buildStandardLevel(){
    	builder.buildStandardLevel();
        tiles = builder.getMap();
        pathfinding = builder.getPathfinding();
        this.stairs_down = builder.getStairsDown();
        this.stairs_up = builder.getStairsUp();
	    this.exits = new ArrayList<>();
	    this.isBuilt = true;
    }

    public void build_final_level(){
	    builder.build_final_level();
	    tiles = builder.getMap();
	    pathfinding = builder.getPathfinding();
	    this.stairs_up = builder.getStairsUp();
	    this.exits = new ArrayList<>();
	    this.isBuilt = true;
    }

    public int width(){
    	return tiles.length;
    }

    public int height(){
    	return tiles[0].length;
    }

    public void initializePathFinding() {
        pathfinding = new char[tiles.length][tiles[0].length];
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                this.pathfinding[i][j] = tiles[i][j].sprite.character;
            }
        }
    }

    public Integer entityAt(Coord location){
    	for(Integer actor : entities){
    		if (location.equals(entityManager.gc(actor, Position.class).location)){
    			return actor;
		    }
	    }

	    return null;
    }

    public boolean isExit(Coord location){

    	for(Exit exit : exits){
    		if(exit.exit_location.equals(location)){
    			return true;
		    }
	    }

	    return false;
    }

    public Exit findExit(Coord location){
    	for(Exit exit : exits){
    		if(exit.exit_location.equals(location)){
    			return exit;
		    }
	    }

	    return null;
    }

    public boolean isPassable(Coord start, Coord direction){
    	return tiles[start.x + direction.x][start.y + direction.y].passable;
	}

	public boolean isSolid(int x, int y){
    	return !tiles[x][y].passable;
	}

	public boolean isOpenable(Coord start, Coord direction){
		return tiles[start.x + direction.x][start.y + direction.y].openable;
	}

	public void open_door(Coord start, Coord direction){
    	tiles[start.x + direction.x][start.y + direction.y] = new Tile((JSONObject)tile_file.get("door - open"));
	}

	public int getCost(Coord start, Coord direction){
    	return tiles[start.x + direction.x][start.y + direction.y].movement_cost;
	}

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }
}
