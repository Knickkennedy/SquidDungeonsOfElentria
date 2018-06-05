package roguelike.utilities;

import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Point {

    public static final Coord NORTH_WEST = Coord.get(-1, -1);
    public static final Coord NORTH = Coord.get(0, -1);
    public static final Coord NORTH_EAST = Coord.get(1, -1);
    public static final Coord EAST = Coord.get(1, 0);
    public static final Coord SOUTH_EAST = Coord.get(1, 1);
    public static final Coord SOUTH = Coord.get(0, 1);
    public static final Coord SOUTH_WEST = Coord.get(-1, 1);
    public static final Coord WEST = Coord.get(-1, 0);
    public static final List<Coord> cardinal = Arrays.asList(NORTH, EAST, WEST, SOUTH);
    public static final List<Coord> direction = Arrays.asList(NORTH_WEST, NORTH, NORTH_EAST,
            EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST);
    public static final Coord WAIT = Coord.get(0, 0);
    
    private Point(){}
    public static Coord getNeighbor(Coord me, Coord direction) {

        return me.add(direction);
    }
    
    public static List<Coord> cardinalNeighbors(Coord me) {
        List<Coord> neighbors = new ArrayList<>();
        for (Coord direction : cardinal) {
            neighbors.add(me.add(direction));
        }
        return neighbors;
    }

    public static List<Coord> getDirectionalNeighbors(Coord me, Coord direction) {

        if (direction.equals(NORTH)) return getNorthNeighbors(me);
        if (direction.equals(SOUTH)) return getSouthNeighbors(me);
        if (direction.equals(EAST)) return getEastNeighbors(me);
        if (direction.equals(WEST)) return getWestNeighbors(me);

        return null;
    }

    public static List<Coord> getFrontierNeighbors(Coord me, Coord direction) {

        if (direction.equals(NORTH)) return getNorth(me);
        if (direction.equals(SOUTH)) return getSouth(me);
        if (direction.equals(EAST)) return getEast(me);
        if (direction.equals(WEST)) return getWest(me);

        return null;
    }

    public static List<Coord> getNorth(Coord me) {
        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(WEST));
        neighbors.add(me.add(EAST));
        neighbors.add(me.add(NORTH));
        neighbors.add(me.add(NORTH_EAST));
        neighbors.add(me.add(NORTH_WEST));

        return neighbors;
    }

    public static List<Coord> getSouth(Coord me) {
        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(WEST));
        neighbors.add(me.add(EAST));
        neighbors.add(me.add(SOUTH));
        neighbors.add(me.add(SOUTH_EAST));
        neighbors.add(me.add(SOUTH_WEST));

        return neighbors;
    }

    public static List<Coord> getEast(Coord me) {
        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(NORTH));
        neighbors.add(me.add(EAST));
        neighbors.add(me.add(SOUTH));
        neighbors.add(me.add(SOUTH_EAST));
        neighbors.add(me.add(NORTH_EAST));

        return neighbors;
    }

    public static List<Coord> getWest(Coord me) {
        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(NORTH));
        neighbors.add(me.add(WEST));
        neighbors.add(me.add(SOUTH));
        neighbors.add(me.add(SOUTH_WEST));
        neighbors.add(me.add(NORTH_WEST));

        return neighbors;
    }

    public static List<Coord> getWestNeighbors(Coord me) {

        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(WEST));
        neighbors.add(me.add(NORTH_WEST));
        neighbors.add(me.add(SOUTH_WEST));
        neighbors.add(me.translate(-2,  0));
        neighbors.add(me.translate(-2, -1));
        neighbors.add(me.translate(-2,  1));

        return neighbors;
    }

    public static List<Coord> getEastNeighbors(Coord me) {

        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(EAST));
        neighbors.add(me.add(NORTH_EAST));
        neighbors.add(me.add(SOUTH_EAST));
        neighbors.add(me.translate(2,  0));
        neighbors.add(me.translate(2, -1));
        neighbors.add(me.translate(2,  1));
        return neighbors;
    }

    public static List<Coord> getSouthNeighbors(Coord me) {

        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(SOUTH));
        neighbors.add(me.add(SOUTH_WEST));
        neighbors.add(me.add(SOUTH_EAST));
        neighbors.add(me.translate( 0, 2));
        neighbors.add(me.translate(-1, 2));
        neighbors.add(me.translate( 1, 2));
        return neighbors;
    }

    public static List<Coord> getNorthNeighbors(Coord me) {

        List<Coord> neighbors = new ArrayList<>();
        neighbors.add(me.add(NORTH));
        neighbors.add(me.add(NORTH_WEST));
        neighbors.add(me.add(NORTH_EAST));
        neighbors.add(me.translate( 0, -2));
        neighbors.add(me.translate(-1, -2));
        neighbors.add(me.translate( 1, -2));
        return neighbors;
    }
}