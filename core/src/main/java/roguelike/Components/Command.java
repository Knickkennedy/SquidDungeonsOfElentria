package roguelike.Components;

import lombok.Getter;
import roguelike.Actions.Action;
import roguelike.Actions.Exit_Through;
import roguelike.Actions.Move;
import roguelike.engine.Game;
import roguelike.engine.Message_Log;
import roguelike.screens.Equipment_Screen;
import roguelike.screens.Inventory_Screen;
import roguelike.utilities.Point;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.Generation.World.entityManager;

@Getter
public class Command extends SquidInput implements Component {

	private Integer entity;
	private Game game;
	private SparseLayers display;
	private Action action;

	private Equipment_Screen equipment_screen;
	private Inventory_Screen inventory_screen;

	public Command(final Integer entity, Game game, SparseLayers display) {
		super();
		this.entity = entity;
		this.game = game;
		this.display = display;
		setKeyHandler(new KH());
		setRepeatGap(160);

		this.equipment_screen = new Equipment_Screen(entity, game);
		this.inventory_screen = new Inventory_Screen(entity, game);
	}

	private class KH implements KeyHandler
	{

			@Override
			public void handle(char key, boolean alt, boolean ctrl, boolean shift) {
			switch(key){
				case DOWN_LEFT_ARROW:
					action = new Move(entity, Point.SOUTH_WEST, display); break;
				case DOWN_ARROW:
					action = new Move(entity, Point.SOUTH, display); break;
				case DOWN_RIGHT_ARROW:
					action = new Move(entity, Point.SOUTH_EAST, display); break;
				case LEFT_ARROW:
					action = new Move(entity, Point.WEST, display); break;
				case CENTER_ARROW:
					action = new Move(entity, Point.WAIT, display); break;
				case RIGHT_ARROW:
					action = new Move(entity, Point.EAST, display); break;
				case UP_LEFT_ARROW:
					action = new Move(entity, Point.NORTH_WEST, display); break;
				case UP_ARROW:
					action = new Move(entity, Point.NORTH, display); break;
				case UP_RIGHT_ARROW:
					action = new Move(entity, Point.NORTH_EAST, display); break;
				case ENTER:
					action = new Exit_Through(entity, display); break;
				case 'e':
					lastKeyCode = -1;
					action = null;
					game.setScreen(equipment_screen); break;
				case 'i':
					lastKeyCode = -1;
					action = null;
					game.setScreen(inventory_screen); break;
				default:
					return;
			}

			Message_Log.getInstance().ticks++;
			Message_Log.getInstance().check_ticks();
			entityManager.gc(entity, Action_Component.class).setAction(action);
		}
	}
}