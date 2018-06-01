package roguelike.Actions;

import roguelike.Components.Action_Component;
import roguelike.Components.Energy;
import roguelike.Components.Position;
import roguelike.Components.Vision;
import roguelike.utilities.Point;

import static roguelike.Generation.World.entityManager;

public class Move extends Action{

	public Point direction;
	private Integer entity;

	public Move(Integer entity, Point direction){
		this.direction = direction;
		this.entity = entity;
		this.cost = entityManager.gc(entity, Position.class).getMap().getCost(entityManager.gc(entity, Position.class).getLocation(), direction);
	}

	@Override
	public boolean perform() {

		if(entityManager.gc(entity, Position.class).getMap().isPassable(entityManager.gc(entity, Position.class).getLocation(), direction)){

			entityManager.gc(entity, Energy.class).energy -= cost;

			entityManager.gc(entity, Position.class).setLocation(direction);
			entityManager.gc(entity, Vision.class).setLocation(direction);
			entityManager.gc(entity, Action_Component.class).setAction(null);

			return true;
		}
		else{

			entityManager.gc(entity, Action_Component.class).setAction(null);

			return false;
		}
	}
}
