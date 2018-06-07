package roguelike.Actions;

import roguelike.Components.*;
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
		Set<Integer> active_actors = entityManager.getAllEntitiesPossessingComponent(Active.class);

		for(Integer actor : active_actors){

			Coord attacker_location = entityManager.gc(entity, Position.class).location;
			attacker_location = attacker_location.add(direction);
			Coord victim_location = entityManager.gc(actor, Position.class).location;

			if(attacker_location.equals(victim_location) && !entity.equals(actor)){
				entityManager.gc(entity, Action_Component.class).setAction(new Attack(entity, actor));

				return false;
			}
		}

		if(entityManager.gc(entity, Energy.class).energy < cost)
			return true;

		if(entityManager.gc(entity, Position.class).map.isPassable(entityManager.gc(entity, Position.class).location, direction)){

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
