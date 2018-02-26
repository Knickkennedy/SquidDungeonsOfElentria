package roguelike.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Point {

    public static final Point NORTH_WEST = new Point(-1, -1);
    public static final Point NORTH = new Point(0, -1);
    public static final Point NORTH_EAST = new Point(1, -1);
    public static final Point EAST = new Point(1, 0);
    public static final Point SOUTH_EAST = new Point(1, 1);
    public static final Point SOUTH = new Point(0, 1);
    public static final Point SOUTH_WEST = new Point(-1, 1);
    public static final Point WEST = new Point(-1, 0);
    public static final List<Point> cardinal = Arrays.asList(NORTH, EAST, WEST, SOUTH);
    public static final List<Point> direction = Arrays.asList(NORTH_WEST, NORTH, NORTH_EAST,
            EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST);
    public static final Point WAIT = new Point(0, 0);

    public int x;
    public int y;
    public Point directionFromParent;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, Point directionFromParent) {
        this.x = x;
        this.y = y;
        this.directionFromParent = directionFromParent;
    }

    public Point getNeighbor(Point direction) {

        return new Point(x + direction.x, y + direction.y, direction);
    }

    public java.awt.Point getNeighbor(java.awt.Point direction) {
        return new java.awt.Point(x + direction.x, y + direction.y);
    }

    public List<Point> cardinalNeighbors() {
        List<Point> neighbors = new ArrayList<>();
        for (Point direction : cardinal) {
            neighbors.add(getNeighbor(direction));
        }
        return neighbors;
    }

    public List<Point> getDirectionalNeighbors(Point direction) {

        if (direction.equals(NORTH)) return getNorthNeighbors();
        if (direction.equals(SOUTH)) return getSouthNeighbors();
        if (direction.equals(EAST)) return getEastNeighbors();
        if (direction.equals(WEST)) return getWestNeighbors();

        return null;
    }

    public List<Point> getFrontierNeighbors(Point direction) {

        if (direction.equals(NORTH)) return getNorth();
        if (direction.equals(SOUTH)) return getSouth();
        if (direction.equals(EAST)) return getEast();
        if (direction.equals(WEST)) return getWest();

        return null;
    }

    public List<Point> getNorth() {
        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(WEST));
        neighbors.add(getNeighbor(EAST));
        neighbors.add(getNeighbor(NORTH));
        neighbors.add(getNeighbor(NORTH_EAST));
        neighbors.add(getNeighbor(NORTH_WEST));

        return neighbors;
    }

    public List<Point> getSouth() {
        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(WEST));
        neighbors.add(getNeighbor(EAST));
        neighbors.add(getNeighbor(SOUTH));
        neighbors.add(getNeighbor(SOUTH_EAST));
        neighbors.add(getNeighbor(SOUTH_WEST));

        return neighbors;
    }

    public List<Point> getEast() {
        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(NORTH));
        neighbors.add(getNeighbor(EAST));
        neighbors.add(getNeighbor(SOUTH));
        neighbors.add(getNeighbor(SOUTH_EAST));
        neighbors.add(getNeighbor(NORTH_EAST));

        return neighbors;
    }

    public List<Point> getWest() {
        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(NORTH));
        neighbors.add(getNeighbor(WEST));
        neighbors.add(getNeighbor(SOUTH));
        neighbors.add(getNeighbor(SOUTH_WEST));
        neighbors.add(getNeighbor(NORTH_WEST));

        return neighbors;
    }

    public List<Point> getWestNeighbors() {

        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(WEST));
        neighbors.add(getNeighbor(NORTH_WEST));
        neighbors.add(getNeighbor(SOUTH_WEST));
        neighbors.add(new Point(this.x - 2, this.y));
        neighbors.add(new Point(this.x - 2, this.y - 1));
        neighbors.add(new Point(this.x - 2, this.y + 1));

        return neighbors;
    }

    public List<Point> getEastNeighbors() {

        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(EAST));
        neighbors.add(getNeighbor(NORTH_EAST));
        neighbors.add(getNeighbor(SOUTH_EAST));
        neighbors.add(new Point(this.x + 2, this.y));
        neighbors.add(new Point(this.x + 2, this.y - 1));
        neighbors.add(new Point(this.x + 2, this.y + 1));

        return neighbors;
    }

    public List<Point> getSouthNeighbors() {

        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(SOUTH));
        neighbors.add(getNeighbor(SOUTH_WEST));
        neighbors.add(getNeighbor(SOUTH_EAST));
        neighbors.add(new Point(this.x, this.y + 2));
        neighbors.add(new Point(this.x - 1, this.y + 2));
        neighbors.add(new Point(this.x + 1, this.y + 2));

        return neighbors;
    }

    public List<Point> getNorthNeighbors() {

        List<Point> neighbors = new ArrayList<>();
        neighbors.add(getNeighbor(NORTH));
        neighbors.add(getNeighbor(NORTH_WEST));
        neighbors.add(getNeighbor(NORTH_EAST));
        neighbors.add(new Point(this.x, this.y - 2));
        neighbors.add(new Point(this.x - 1, this.y - 2));
        neighbors.add(new Point(this.x + 1, this.y - 2));

        return neighbors;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", this.x, this.y);
    }

    @Override
    public boolean equals(Object obj) {

        Point p = (Point) obj;
        return this.x == p.x && this.y == p.y;
    }

    public void flipHorizontally() {
        this.x *= -1;
    }

    public void flipVertically() {
        this.y *= -1;
    }

}