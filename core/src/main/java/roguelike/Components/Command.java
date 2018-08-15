package roguelike.Components;

import lombok.Getter;
import roguelike.Actions.Action;
import roguelike.Actions.Animations.Animation;
import roguelike.Actions.ExitThrough;
import roguelike.Actions.Move;
import roguelike.Generation.World;
import roguelike.engine.Game;
import roguelike.engine.MessageLog;
import roguelike.screens.EquipmentScreen;
import roguelike.screens.InventoryScreen;
import roguelike.screens.StartScreen;
import roguelike.screens.TargetingScreen;
import roguelike.utilities.Point;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import java.util.List;

import static roguelike.Generation.World.entityManager;

@Getter
public class Command extends SquidInput implements Component {

	private Integer entity;
	private Game game;
	private SparseLayers display;
	private List<Animation> animations;
	private World world;
	private Action action;

	private EquipmentScreen equipment_screen;
	private InventoryScreen inventory_screen;
	private TargetingScreen targetingScreen;

	public Command(final Integer entity, Game game, SparseLayers display, World world, List<Animation> animations) {
		super();
		this.entity = entity;
		this.game = game;
		this.display = display;
		this.world = world;
		this.animations = animations;
		setKeyHandler(new KH());
		setRepeatGap(160);

		this.equipment_screen = new EquipmentScreen(entity, game);
		this.inventory_screen = new InventoryScreen(entity, game);
		this.targetingScreen = new TargetingScreen(game, world, entity, animations);
	}

	private class KH implements KeyHandler
	{

			@Override
			public void handle(char key, boolean alt, boolean ctrl, boolean shift) {
			switch(key){
				case DOWN_LEFT_ARROW:
					action = new Move(entity, Point.SOUTH_WEST, display, animations); break;
				case DOWN_ARROW:
					action = new Move(entity, Point.SOUTH, display, animations); break;
				case DOWN_RIGHT_ARROW:
					action = new Move(entity, Point.SOUTH_EAST, display, animations); break;
				case LEFT_ARROW:
					action = new Move(entity, Point.WEST, display, animations); break;
				case CENTER_ARROW:
					action = new Move(entity, Point.WAIT, display, animations); break;
				case RIGHT_ARROW:
					action = new Move(entity, Point.EAST, display, animations); break;
				case UP_LEFT_ARROW:
					action = new Move(entity, Point.NORTH_WEST, display, animations); break;
				case UP_ARROW:
					action = new Move(entity, Point.NORTH, display, animations); break;
				case UP_RIGHT_ARROW:
					action = new Move(entity, Point.NORTH_EAST, display, animations); break;
				case ENTER:
					action = new ExitThrough(entity, display); break;
				case 'e':
					lastKeyCode = -1;
					action = null;
					game.setScreen(equipment_screen); break;
				case 'i':
					lastKeyCode = -1;
					action = null;
					game.setScreen(inventory_screen); break;
				case 't':
					lastKeyCode = -1;
					action = null;
					game.setScreen(targetingScreen); break;
				default:
					return;
			}

			MessageLog.getInstance().ticks++;
			MessageLog.getInstance().check_ticks();
			entityManager.gc(entity, ActionComponent.class).setAction(action);
		}
	}
}