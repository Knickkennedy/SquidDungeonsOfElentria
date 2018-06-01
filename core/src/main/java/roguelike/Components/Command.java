package roguelike.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import lombok.Getter;
import roguelike.Actions.Action;
import roguelike.Actions.Move;
import roguelike.utilities.Point;

import static roguelike.Generation.World.entityManager;

@Getter
public class Command extends Component implements InputProcessor{

	private Integer entity;
	private Action action;
	public Command(final Integer entity){
		this.entity = entity;
	}

	@Override
	public boolean keyDown(int keycode) {

		switch(keycode){
			case Input.Keys.NUMPAD_1:
				action = new Move(entity, Point.SOUTH_WEST);
				break;
			case Input.Keys.DOWN:
			case Input.Keys.NUMPAD_2:
				action = new Move(entity,Point.SOUTH);
				break;
			case Input.Keys.NUMPAD_3:
				action = new Move(entity,Point.SOUTH_EAST);
				break;
			case Input.Keys.LEFT:
			case Input.Keys.NUMPAD_4:
				action = new Move(entity,Point.WEST);
				break;
			case Input.Keys.RIGHT:
			case Input.Keys.NUMPAD_6:
				action = new Move(entity,Point.EAST);
				break;
			case Input.Keys.NUMPAD_7:
				action = new Move(entity,Point.NORTH_WEST);
				break;
			case Input.Keys.UP:
			case Input.Keys.NUMPAD_8:
				action = new Move(entity,Point.NORTH);
				break;
			case Input.Keys.NUMPAD_9:
				action = new Move(entity,Point.NORTH_EAST);
				break;
			//case Input.Keys.NUMPAD_5:
			default:
				action = new Move(entity,Point.WAIT);
				break;
		}
		entityManager.gc(entity, Action_Component.class).setAction(action);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
