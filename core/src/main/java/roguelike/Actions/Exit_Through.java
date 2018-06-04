package roguelike.Actions;

import roguelike.Components.Action_Component;
import roguelike.Components.Energy;
import roguelike.Components.Position;
import roguelike.Components.Vision;
import roguelike.Generation.Exit;
import roguelike.utilities.Point;

import static roguelike.Generation.World.entityManager;

public class Exit_Through extends Action{

	private Integer entity;

	public Exit_Through(Integer entity){
		this.entity = entity;
		this.cost = entityManager.gc(entity, Position.class).map.getCost(entityManager.gc(entity, Position.class).location, Point.WAIT);
	}

	@Override
	public boolean perform() {
		Point temp_position = entityManager.gc(entity, Position.class).location;

		System.out.println(entityManager.gc(entity, Position.class).map.findExit(temp_position).floor);

		if(entityManager.gc(entity, Position.class).map.isExit(temp_position)){

			entityManager.gc(entity, Energy.class).energy -= cost;

			entityManager.gc(entity, Position.class).map.findExit(temp_position).set_player_location();
			Exit exit = entityManager.gc(entity, Position.class).map.findExit(temp_position);

			Position position = new Position(exit.go_through());

			entityManager.addComponent(entity, position);
			entityManager.gc(entity, Position.class).location = exit.player_coordinates;

			Vision vision = new Vision(entityManager.gc(entity, Position.class).location, entityManager.gc(entity, Position.class).map, 5);
			entityManager.addComponent(entity, vision);

			entityManager.gc(entity, Action_Component.class).setAction(null);
			return true;
		}


		entityManager.gc(entity, Action_Component.class).setAction(null);

		return false;
	}
}
