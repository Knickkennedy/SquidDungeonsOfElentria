package roguelike.Actions;

import roguelike.Components.*;
import roguelike.Generation.Exit;
import roguelike.Generation.Map;
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
		
		if(entityManager.gc(entity, Position.class).map.isExit(temp_position)){

			entityManager.gc(entity, Energy.class).energy -= cost;

			remove_active_flag(entityManager.gc(entity, Position.class).map);
			entityManager.gc(entity, Position.class).map.entities.remove(entity);

			entityManager.gc(entity, Position.class).map.findExit(temp_position).set_player_location();
			Exit exit = entityManager.gc(entity, Position.class).map.findExit(temp_position);

			Position position = new Position(exit.go_through());
			position.map.entities.add(entity);
			position.location = exit.player_coordinates;
			entityManager.addComponent(entity, position);

			add_active_flag(entityManager.gc(entity, Position.class).map);

			Vision vision = new Vision(position.location, position.map, 5);
			entityManager.addComponent(entity, vision);

			entityManager.gc(entity, Action_Component.class).setAction(null);
			return true;
		}


		entityManager.gc(entity, Action_Component.class).setAction(null);

		return false;
	}

	public void remove_active_flag(Map map){
		for(Integer entity : map.entities){
			entityManager.remove_component(entity, Active.class);
		}
	}

	public void add_active_flag(Map map){
		for(Integer entity : map.entities){
			entityManager.addComponent(entity, new Active());
		}
	}
}

