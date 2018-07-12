package roguelike.Actions;

import roguelike.Components.*;
import roguelike.utilities.Point;
import squidpony.squidmath.Coord;

import static roguelike.Generation.World.entityManager;

public class Move extends Action{

	public Coord direction;
	private Integer entity;

	public Move(Integer entity, Coord direction){
		this.direction = direction;
		this.entity = entity;
		this.cost = entityManager.gc(entity, Position.class).map.getCost(entityManager.gc(entity, Position.class).location, direction);
	}

	@Override
	public boolean perform() {
		Coord location = entityManager.gc(entity, Position.class).location;
		if(entityManager.gc(entity, Position.class).map.entityAt(location.add(direction)) != null
				&& direction != Point.WAIT){

			entityManager.gc(entity, Action_Component.class).setAction(
					new Melee_Attack(entity, entityManager.gc(entity, Position.class).map
							.entityAt(location.add(direction))));
			return false;
		}
		else if(entityManager.gc(entity, Position.class).map.
				isPassable(location, direction)){

			if(entityManager.gc(entity, Energy.class).energy < cost)
				return true;
			entityManager.gc(entity, Energy.class).energy -= cost;
			entityManager.display.slide(entityManager.gc(entity, Sprite.class)
					.makeGlyph(entityManager.display, location.x, location.y), location.x, location.y + 2, location.x + direction.x, location.y + direction.y + 2, 0.2f, () ->
					{


						entityManager.gc(entity, Position.class).update_location(direction);

						if (entityManager.gc(entity, Vision.class) != null) {
							entityManager.gc(entity, Vision.class).setLocation(entityManager.gc(entity, Position.class).location);
						}
					}
			);

			entityManager.gc(entity, Action_Component.class).setAction(null);

			return true;
		}
		else if(entityManager.gc(entity, Position.class).map.isOpenable(entityManager.gc(entity, Position.class).location, direction)){
			entityManager.gc(entity, Action_Component.class).setAction(new Open_Door(entity, direction));

			return false;
		}
		else{

			entityManager.gc(entity, Action_Component.class).setAction(null);
			return false;
		}
	}
}
