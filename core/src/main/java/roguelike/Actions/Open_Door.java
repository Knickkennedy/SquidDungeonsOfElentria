package roguelike.Actions;

import roguelike.Components.Action_Component;
import roguelike.Components.Energy;
import roguelike.Components.Position;
import roguelike.Components.Vision;
import squidpony.squidmath.Coord;

import static roguelike.Generation.World.entityManager;

public class Open_Door extends Action{
	private Integer entity;
	private Coord direction;

	public Open_Door(Integer entity, Coord direction){
		this.entity = entity;
		this.direction = direction;
		this.cost = entityManager.gc(entity, Position.class).map.getCost(entityManager.gc(entity, Position.class).location, direction);
	}

	@Override
	public boolean perform() {

		entityManager.gc(entity, Energy.class).energy -= cost;

		entityManager.gc(entity, Position.class).map.open_door(entityManager.gc(entity, Position.class).location, direction);

		entityManager.gc(entity, Position.class).update_location(direction);
		entityManager.gc(entity, Vision.class).setLocation(entityManager.gc(entity, Position.class).location);
		entityManager.gc(entity, Action_Component.class).setAction(null);

		return true;
	}
}
