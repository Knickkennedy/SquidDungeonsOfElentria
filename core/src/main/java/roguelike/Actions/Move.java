package roguelike.Actions;

import roguelike.Actions.Animations.Animation;
import roguelike.Actions.Animations.MoveAnimation;
import roguelike.Components.*;
import roguelike.utilities.Point;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.TextCellFactory;
import squidpony.squidmath.Coord;

import java.util.List;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.message_buffer;

public class Move extends Action{

	private SparseLayers display;
	private List<Animation> animations;
	private Coord direction;
	private Integer entity;

	public Move(Integer entity, Coord direction, SparseLayers display, List<Animation> animations){
		this.display = display;
		this.animations = animations;
		TextCellFactory textCellFactory = new TextCellFactory();
		textCellFactory.initByFont();
		this.direction = direction;
		this.entity = entity;
		this.cost = entityManager.gc(entity, Position.class).map.getCost(entityManager.gc(entity, Position.class).location, direction);
	}

	@Override
	public boolean isAlternativeAction(){
		Position position = entityManager.gc(entity, Position.class);
		ActionComponent actionComponent = entityManager.gc(entity, ActionComponent.class);
		Details details = entityManager.gc(entity, Details.class);
		Integer entityAt = position.map.entityAt(position.location.add(direction));
		if(entityAt != null && direction != Point.WAIT && details.is_hostile_towards(entityAt)){
			actionComponent.setAction(new MeleeAttack(entity, entityAt, display, animations));
			return true;
		}
		else if(entityAt != null && direction != Point.WAIT && !details.is_hostile_towards(entityAt)){
			actionComponent.setAction(null);
			return true;
		}
		else if(position.map.isOpenable(position.location, direction)){
			actionComponent.setAction(new OpenDoor(entity, direction));
			return true;
		}
		else if(!position.map.isPassable(position.location, direction)){
			actionComponent.setAction(null);
			return true;
		}

		return false;
	}

	@Override
	public boolean canPerform(){
		return entityManager.gc(entity, Energy.class).energy >= cost;
	}

	@Override
	public boolean perform() {


		entityManager.gc(entity, Energy.class).energy -= cost;

		Coord start = entityManager.gc(entity, Position.class).location;
		start = Coord.get(start.x, start.y + message_buffer);
		entityManager.gc(entity, Position.class).update_location(direction);

		Coord end = entityManager.gc(entity, Position.class).location;
		end = Coord.get(end.x, end.y + message_buffer);
		Sprite sprite = entityManager.gc(entity, Sprite.class);

		animations.add(new MoveAnimation(sprite, start, end));

		/*if (sprite.getGlyph() != null) {
			display.slide(sprite.getGlyph(), start.x, start.y + message_buffer, end.x, end.y + message_buffer, 0.03f, null);
		}*/

		if (entityManager.gc(entity, Vision.class) != null) {
			entityManager.gc(entity, Vision.class).setLocation(entityManager.gc(entity, Position.class).location);
		}
		entityManager.gc(entity, ActionComponent.class).setAction(null);

		return true;
	}
}
