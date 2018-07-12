package roguelike.Actions;

import roguelike.Components.*;
import roguelike.engine.Message_Log;
import roguelike.utilities.Point;
import squidpony.squidmath.Coord;

import java.util.Set;

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

		if(entityManager.gc(entity, Position.class).map.entityAt(entityManager.gc(entity, Position.class).location.add(direction)) != null
				&& direction != Point.WAIT
				&& entityManager.gc(entity, Details.class)
					.is_hostile_towards(entityManager.gc(entity, Position.class).map
							.entityAt(entityManager.gc(entity, Position.class).location.add(direction)))){

			entityManager.gc(entity, Action_Component.class).setAction(
					new Melee_Attack(entity, entityManager.gc(entity, Position.class).map
							.entityAt(entityManager.gc(entity, Position.class).location.add(direction))));
			return false;
		}
		else if(entityManager.gc(entity, Position.class).map.entityAt(entityManager.gc(entity, Position.class).location.add(direction)) != null
				&& direction != Point.WAIT
				&& !entityManager.gc(entity, Details.class)
				.is_hostile_towards(entityManager.gc(entity, Position.class).map
						.entityAt(entityManager.gc(entity, Position.class).location.add(direction)))){
			entityManager.gc(entity, Action_Component.class).setAction(null);
			return false;
		}
		else if(entityManager.gc(entity, Position.class).map.
				isPassable(entityManager.gc(entity, Position.class).location, direction)){

			if(entityManager.gc(entity, Energy.class).energy < cost)
				return true;

			entityManager.gc(entity, Energy.class).energy -= cost;

			entityManager.gc(entity, Position.class).update_location(direction);

			if(entityManager.gc(entity, Vision.class) != null) {
				entityManager.gc(entity, Vision.class).setLocation(entityManager.gc(entity, Position.class).location);
			}
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
