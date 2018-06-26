package roguelike.Components;

import lombok.Getter;
import roguelike.Actions.Action;
import roguelike.Actions.Exit_Through;
import roguelike.Actions.Move;
import roguelike.engine.Game;
import roguelike.engine.Message_Log;
import roguelike.utilities.Point;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.Generation.World.entityManager;

@Getter
public class Command extends SquidInput implements Component {

	private Integer entity;
	private Game game;
	private Action action;
	public Command(final Integer entity, Game game) {
		super();
		this.entity = entity;
		this.game = game;
		setKeyHandler(new KH());
		setRepeatGap(160);
	}
	private class KH implements KeyHandler
	{
			/**
			 * The only method you need to implement yourself in KeyHandler, this should react to keys such as
			 * 'a' (produced by pressing the A key while not holding Shift), 'E' (produced by pressing the E key while
			 * holding Shift), and '\u2190' (left arrow in unicode, also available as a constant in SquidInput, produced by
			 * pressing the left arrow key even though that key does not have a default unicode representation). Capital
			 * letters will be capitalized when they are passed to this, but they may or may not have the shift argument as
			 * true depending on how this method was called. Symbols that may be produced by holding Shift and pressing a
			 * number or a symbol key can vary between keyboards (some may require Shift to be held down, others may not).
			 * <br>
			 * This can react to the input in whatever way you find appropriate for your game.
			 *
			 * @param key   a char of the "typed" representation of the key, such as 'a' or 'E', or if there is no Unicode
			 *              character for the key, an appropriate alternate character as documented in SquidInput.fromKey()
			 * @param alt   true if the Alt modifier was being held while this key was entered, false otherwise.
			 * @param ctrl  true if the Ctrl modifier was being held while this key was entered, false otherwise.
			 * @param shift true if the Shift modifier was being held while this key was entered, false otherwise.
			 */
			@Override
			public void handle(char key, boolean alt, boolean ctrl, boolean shift) {
			switch(key){
				case DOWN_LEFT_ARROW:
					action = new Move(entity, Point.SOUTH_WEST); break;
				case DOWN_ARROW:
					action = new Move(entity, Point.SOUTH); break;
				case DOWN_RIGHT_ARROW:
					action = new Move(entity, Point.SOUTH_EAST); break;
				case LEFT_ARROW:
					action = new Move(entity, Point.WEST); break;
				case CENTER_ARROW:
					action = new Move(entity, Point.WAIT); break;
				case RIGHT_ARROW:
					action = new Move(entity, Point.EAST); break;
				case UP_LEFT_ARROW:
					action = new Move(entity, Point.NORTH_WEST); break;
				case UP_ARROW:
					action = new Move(entity, Point.NORTH); break;
				case UP_RIGHT_ARROW:
					action = new Move(entity, Point.NORTH_EAST); break;
				case ENTER:
					action = new Exit_Through(entity); break;
				case 'e':
					break;
			}

			Message_Log.getInstance().ticks++;
			Message_Log.getInstance().check_ticks();
			entityManager.gc(entity, Action_Component.class).setAction(action);
		}
	}
}