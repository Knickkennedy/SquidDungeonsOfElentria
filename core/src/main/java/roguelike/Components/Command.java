package roguelike.Components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import lombok.Getter;
import roguelike.Actions.Move;
import roguelike.utilities.Point;

@Getter
public class Command extends Component implements InputProcessor{

	private Integer entity;
	private Move move;
	public Command(final Integer entity){
		this.entity = entity;
		move = new Move(this.entity, Point.WAIT);
	}

	@Override
	public boolean keyDown(int keycode) {

		switch(keycode){
			case Input.Keys.NUMPAD_1:
				move.direction = Point.SOUTH_WEST;
				break;
			case Input.Keys.DOWN:
			case Input.Keys.NUMPAD_2:
				move.direction = Point.SOUTH;
				break;
			case Input.Keys.NUMPAD_3:
				move.direction = Point.SOUTH_EAST;
				break;
			case Input.Keys.LEFT:
			case Input.Keys.NUMPAD_4:
				move.direction = Point.WEST;
				break;
			case Input.Keys.RIGHT:
			case Input.Keys.NUMPAD_6:
				move.direction = Point.EAST;
				break;
			case Input.Keys.NUMPAD_7:
				move.direction = Point.NORTH_WEST;
				break;
			case Input.Keys.UP:
			case Input.Keys.NUMPAD_8:
				move.direction = Point.NORTH;
				break;
			case Input.Keys.NUMPAD_9:
				move.direction = Point.NORTH_EAST;
				break;
			//case Input.Keys.NUMPAD_5:
			default:
				move.direction = Point.WAIT;
				break;
		}
		//entityManager.gc(entity, Action_Component.class).setAction(move);
		if(!move.direction.equals(Point.WAIT))
			move.perform();
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
