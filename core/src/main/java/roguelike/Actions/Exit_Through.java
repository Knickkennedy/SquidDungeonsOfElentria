package roguelike.Actions;

import roguelike.Components.*;
import roguelike.Generation.Exit;
import roguelike.Generation.Map;
import roguelike.utilities.Point;
import squidpony.squidmath.Coord;

import java.util.Set;

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
			//clears all Glyphs and ends any actions they are processing
//			for (; !entityManager.display.glyphs.isEmpty();) {
//				entityManager.display.removeGlyph(entityManager.display.glyphs.get(entityManager.display.glyphs.size()-1));
//			}
			Set<Integer> ents = entityManager.getAllEntitiesPossessingComponent(Sprite.class);
			for(Integer i : ents)
			{
				if(entity.equals(i))
					continue;
				Sprite sprite = entityManager.gc(i, Sprite.class);
				if(sprite != null && sprite.glyph != null) 
				{
					entityManager.display.removeGlyph(sprite.glyph);
					sprite.glyph = null;
				}
			}
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
			entityManager.gc(entity, Sprite.class).makeGlyph(entityManager.display, position.location.x, position.location.y);
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

