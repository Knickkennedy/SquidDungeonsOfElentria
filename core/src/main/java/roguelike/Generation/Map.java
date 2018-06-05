package roguelike.Generation;

import com.badlogic.gdx.Gdx;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import squidpony.squidmath.Coord;

import java.io.IOException;
import java.util.ArrayList;

public class Map{

    private Tile[][] tiles;
    public char[][] pathfinding;

    private JSONObject tile_file;

    public ArrayList<Exit> exits;

    private Map_Builder builder;
    public Coord stairs_down;
    public Coord stairs_up;

    public Map(final Tile[][] tiles) {
        this.tiles = tiles;
        this.exits = new ArrayList<>();
        initializePathFinding();

	    JSONParser parser = new JSONParser();
	    try {
		    this.tile_file = (JSONObject)parser.parse(Gdx.files.internal("tiles.txt").reader());
	    } catch (IOException | ParseException e) {
		    e.printStackTrace();
	    }
    }

    public Map(final int width, final int height) {
        builder = new Map_Builder(width, height);
        tile_file = builder.tile_file;
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
    }

    public void build_final_level(){
	    builder.buildStandardLevel();
	    tiles = builder.getMap();
	    pathfinding = builder.getPathfinding();
	    this.stairs_up = builder.getStairsUp();
	    this.exits = new ArrayList<>();
    }

    public void initializePathFinding() {
        pathfinding = new char[tiles.length][tiles[0].length];
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                this.pathfinding[i][j] = this.tiles[i][j].passable ? '.' : '#';
            }
        }
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
