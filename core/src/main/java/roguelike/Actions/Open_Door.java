package roguelike.Actions;

import roguelike.Components.*;
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

		Coord location = entityManager.gc(entity, Position.class).location;
		entityManager.display.slide(entityManager.gc(entity, Sprite.class)
						.makeGlyph(entityManager.display, location.x, location.y + 2), location.x, location.y + 2, location.x, location.y + 2, 0.15f, null);
		entityManager.gc(entity, Position.class).map.open_door(location, direction);
		entityManager.gc(entity, Vision.class).setLocation(location);
		
		entityManager.gc(entity, Action_Component.class).setAction(null);

		return true;
	}
}
