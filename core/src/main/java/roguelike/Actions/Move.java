package roguelike.Actions;

import roguelike.Components.Action_Component;
import roguelike.Components.Energy;
import roguelike.Components.Position;
import roguelike.utilities.Point;

import static roguelike.Generation.World.entityManager;

public class Move extends Action{

	private Point direction;
	private Integer entity;

	public Move(Integer entity, Point direction){
		this.direction = direction;
		this.entity = entity;
	}

	@Override
	public boolean perform() {

		if(entityManager.gc(entity, Energy.class).energy < entityManager.gc(entity, Position.class).getMap().getCost(entityManager.gc(entity, Position.class).getLocation(), direction)) {
			entityManager.gc(entity, Action_Component.class).setAction(this);

			return true;
		}

		if(entityManager.gc(entity, Position.class).getMap().isPassable(entityManager.gc(entity, Position.class).getLocation(), direction)){
			entityManager.gc(entity, Position.class).setLocation(direction);
			entityManager.gc(entity, Action_Component.class).setAction(null);
			entityManager.gc(entity, Energy.class).energy -= entityManager.gc(entity, Position.class).getMap().getCost(entityManager.gc(entity, Position.class).getLocation(), direction);

			return true;
		}
		else{
			entityManager.gc(entity, Action_Component.class).setAction(null);

			return false;
		}
	}
}
