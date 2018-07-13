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
		Position position = entityManager.gc(entity, Position.class);
		if(position == null) return true;
		if(!position.map.equals(entityManager.gc(entityManager.getPlayer(), Position.class).map))
			return true;
		Coord location = position.location;
		if(entityManager.gc(entity, Position.class).map.entityAt(location.add(direction)) != null
				&& direction != Point.WAIT
				&& entityManager.gc(entity, Details.class)
					.is_hostile_towards(entityManager.gc(entity, Position.class).map
							.entityAt(location.add(direction)))){
			entityManager.gc(entity, Action_Component.class).setAction(
					new Melee_Attack(entity, entityManager.gc(entity, Position.class).map
							.entityAt(location.add(direction))));
			return false;
		}
		else if(entityManager.gc(entity, Position.class).map.entityAt(location.add(direction)) != null
				&& direction != Point.WAIT
				&& !entityManager.gc(entity, Details.class)
				.is_hostile_towards(entityManager.gc(entity, Position.class).map
						.entityAt(location.add(direction)))){
			entityManager.gc(entity, Action_Component.class).setAction(null);
			return false;
		}
		else if(entityManager.gc(entity, Position.class).map.
				isPassable(location, direction)) {

			if (entityManager.gc(entity, Energy.class).energy < cost)
				return true;
			entityManager.gc(entity, Energy.class).energy -= cost;
			Sprite sprite = entityManager.gc(entity, Sprite.class);
			if(sprite != null && sprite.glyph != null) 
				entityManager.display.slide(sprite.glyph, location.x, location.y + 2,
					location.x + direction.x, location.y + direction.y + 2, 0.15f, null);
			Position pos = entityManager.gc(entity, Position.class);
			if (pos != null)
				pos.update_location(direction);

			if (entityManager.gc(entity, Vision.class) != null) {
				entityManager.gc(entity, Vision.class).setLocation(location);
			}
			entityManager.gc(entity, Action_Component.class).setAction(null);

			return true;
		}
		else if(entityManager.gc(entity, Position.class).map.isOpenable(location, direction)){
			entityManager.gc(entity, Action_Component.class).setAction(new Open_Door(entity, direction));

			return false;
		}
		else{

			entityManager.gc(entity, Action_Component.class).setAction(null);
			return false;
		}
	}
}
