package roguelike.Actions;

import roguelike.Components.Action_Component;
import roguelike.Components.Energy;
import roguelike.Components.Position;
import roguelike.Components.Vision;
import roguelike.Generation.Exit;
import roguelike.utilities.Point;
import squidpony.squidmath.Coord;

import static roguelike.Generation.World.entityManager;

public class Exit_Through extends Action{

	private Integer entity;

	public Exit_Through(Integer entity){
		this.entity = entity;
		this.cost = entityManager.gc(entity, Position.class).map.getCost(entityManager.gc(entity, Position.class).location, Point.WAIT);
	}

	@Override
	public boolean perform() {
		Coord temp_position = entityManager.gc(entity, Position.class).location;
		if(temp_position != null) 
			System.out.println(entityManager.gc(entity, Position.class).map.findExit(temp_position).floor);
		else
		{
			System.out.println("temp_position is null");
			return false;
		}
		
		if(entityManager.gc(entity, Position.class).map.isExit(temp_position)){

			entityManager.gc(entity, Energy.class).energy -= cost;

			entityManager.gc(entity, Position.class).map.findExit(temp_position).set_player_location();
			Exit exit = entityManager.gc(entity, Position.class).map.findExit(temp_position);

			Position position = new Position(exit.go_through());
			position.location = exit.player_coordinates;
			entityManager.addComponent(entity, position);

			Vision vision = new Vision(position.location, position.map, 5);
			entityManager.addComponent(entity, vision);

			entityManager.gc(entity, Action_Component.class).setAction(null);
			return true;
		}


		entityManager.gc(entity, Action_Component.class).setAction(null);

		return false;
	}
}
