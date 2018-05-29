package roguelike.Generation;

import org.json.simple.parser.ParseException;
import roguelike.utilities.Point;

import java.io.IOException;

public class Map{

    private Tile[][] tiles;
    public char[][] pathfinding;

    private Map_Builder builder;
    public java.awt.Point stairs_down;
    public java.awt.Point stairs_up;

    public Map(final Tile[][] tiles) {
        this.tiles = tiles;
        initializePathFinding();
    }

    public Map(final int width, final int height) throws IOException, ParseException {
        this.builder = new Map_Builder(width, height);
    }

    public Tile getTileAt(int x, int y){
        return tiles[x][y];
    }

    public void buildStandardLevel() throws IOException, ParseException {
        builder.buildStandardLevel();
        tiles = builder.getMap();
        pathfinding = builder.getPathfinding();
        this.stairs_down = builder.getStairsDown();
        this.stairs_up = builder.getStairsUp();
    }

    public void initializePathFinding() {
        pathfinding = new char[tiles.length][tiles[0].length];
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                this.pathfinding[i][j] = this.tiles[i][j].getSprite().character;
            }
        }
    }

    public boolean isPassable(Point start, Point direction){
    	return tiles[start.x + direction.x][start.y + direction.y].isPassable();
	}

	public int getCost(Point start, Point direction){
    	return tiles[start.x + direction.x][start.y + direction.y].getMovement_cost();
	}

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }
}
