package roguelike.Actions;

import roguelike.Components.*;
import roguelike.utilities.Point;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.TextCellFactory;
import squidpony.squidmath.Coord;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.message_buffer;

public class Move extends Action{

	private SparseLayers display;
	private Coord direction;
	private Integer entity;

	public Move(Integer entity, Coord direction, SparseLayers display){
		this.display = display;
		TextCellFactory textCellFactory = new TextCellFactory();
		textCellFactory.initByFont();
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

			entityManager.gc(entity, ActionComponent.class).setAction(
					new MeleeAttack(entity, entityManager.gc(entity, Position.class).map
							.entityAt(entityManager.gc(entity, Position.class).location.add(direction)), display));
			return false;
		}
		else if(entityManager.gc(entity, Position.class).map.entityAt(entityManager.gc(entity, Position.class).location.add(direction)) != null
				&& direction != Point.WAIT
				&& !entityManager.gc(entity, Details.class)
				.is_hostile_towards(entityManager.gc(entity, Position.class).map
						.entityAt(entityManager.gc(entity, Position.class).location.add(direction)))){
			entityManager.gc(entity, ActionComponent.class).setAction(null);
			return false;
		}
		else if(entityManager.gc(entity, Position.class).map.
				isPassable(entityManager.gc(entity, Position.class).location, direction)){

			if(entityManager.gc(entity, Energy.class).energy < cost)
				return true;

			entityManager.gc(entity, Energy.class).energy -= cost;

			Coord start = entityManager.gc(entity, Position.class).location;

			entityManager.gc(entity, Position.class).update_location(direction);

			Coord end = entityManager.gc(entity, Position.class).location;
			Sprite sprite = entityManager.gc(entity, Sprite.class);

			if(sprite.getGlyph() != null) {
				display.slide(sprite.getGlyph(), start.x, start.y + message_buffer, end.x, end.y + message_buffer, 0.1f, null);
			}

			if(entityManager.gc(entity, Vision.class) != null) {
				entityManager.gc(entity, Vision.class).setLocation(entityManager.gc(entity, Position.class).location);
			}
			entityManager.gc(entity, ActionComponent.class).setAction(null);

			return true;
		}
		else if(entityManager.gc(entity, Position.class).map.isOpenable(entityManager.gc(entity, Position.class).location, direction)){
			entityManager.gc(entity, ActionComponent.class).setAction(new OpenDoor(entity, direction));

			return false;
		}
		else{

			entityManager.gc(entity, ActionComponent.class).setAction(null);
			return false;
		}
	}
}
