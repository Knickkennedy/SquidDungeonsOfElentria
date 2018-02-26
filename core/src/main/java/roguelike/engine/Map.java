package roguelike.engine;

import roguelike.enums.Tile;

public class Map{

    private Tile[][] tiles;
    public char[][] pathfinding;

    private Map_Builder builder;
    public java.awt.Point stairs_down;
    public java.awt.Point stairs_up;

    private int danger;
    private int level_indicator;

    public Map(final Tile[][] tiles) {
        this.tiles = tiles;
        this.danger = 0;
        initializePathFinding();
    }

    public Map(final int width, final int height) {
        this.danger = 0;
        this.builder = new Map_Builder(width, height);
    }

    public void buildStandardLevel() {
        builder.buildStandardLevel();
        tiles = builder.getMap();
        pathfinding = builder.getPathfinding();
        this.stairs_down = builder.getStairsDown();
        this.stairs_up = builder.getStairsUp();
    }

    public boolean isDownStairs(Point location) {
        return tiles[location.x][location.y] == Tile.Stairs_Down || tiles[location.x][location.y] == Tile.Cave;
    }

    public boolean isDownStairs(java.awt.Point location) {
        return tiles[location.x][location.y] == Tile.Stairs_Down || tiles[location.x][location.y] == Tile.Cave;
    }

    public boolean isUpStairs(Point location) {
        return tiles[location.x][location.y] == Tile.Stairs_Up;
    }



    public void initializePathFinding() {
        pathfinding = new char[tiles.length][tiles[0].length];
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                this.pathfinding[i][j] = this.tiles[i][j].getSprite().character;
            }
        }
    }

    public boolean isFreePosition(final Point location) {
        if (location.x < 0 || location.y < 0) {
            return false;
        }

        if (location.x > tiles.length || location.y > tiles[0].length) {
            return false;
        }

        return !tiles[location.x][location.y].isSolid();
    }

    public boolean isDoor(final java.awt.Point location) {
        return tiles[location.x][location.y] == Tile.Closed_Door;
    }

    public boolean isFreePosition(final java.awt.Point location) {
        if (location.x < 0 || location.y < 0) {
            return false;
        }

        if (location.x > tiles.length || location.y > tiles[0].length) {
            return false;
        }

        return !tiles[location.x][location.y].isSolid();
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }
}
