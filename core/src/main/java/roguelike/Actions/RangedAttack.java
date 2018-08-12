package roguelike.Actions;

import roguelike.Components.ActionComponent;
import roguelike.Components.Energy;
import roguelike.Components.Position;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;

import java.util.Queue;

import static roguelike.Generation.World.entityManager;

public class RangedAttack extends Action{

    private Integer entity; //Entity making attack
    private Queue<Coord> line; //Our initial line of fire
    private Queue<Coord> displayLine;
    private SparseLayers display; //Needed for animations

    public RangedAttack(Integer entity, Queue<Coord> lineIn, SparseLayers display){
        this.entity = entity;
        this.line = lineIn;
        this.display = display;
        this.cost = 1000;
    }

    @Override
    public boolean perform() {

        if(entityManager.gc(entity, Energy.class).energy < cost)
            return true;

        Position position = entityManager.gc(entity, Position.class);
        for(Coord coord : line){

            if(position.map.isSolid(coord.x, coord.y))
                break;

            Integer target = position.map.entityAt(coord);
            if(target != null){

                break;
            }

            displayLine.add(coord);
        }

        entityManager.gc(entity, ActionComponent.class).setAction(null);
        return true;
    }
}
