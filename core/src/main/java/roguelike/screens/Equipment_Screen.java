package roguelike.screens;

import com.badlogic.gdx.graphics.Color;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.SquidInput;

public class Equipment_Screen extends SquidInput implements Screen {

	public Integer entity;
	public Game game;

	public Equipment_Screen(Integer entity, Game game){
		super();
		this.entity = entity;
		this.game = game;
		setKeyHandler(new KH());
		setRepeatGap(200);
	}

	private class KH implements KeyHandler{

		@Override
		public void handle(char key, boolean alt, boolean ctrl, boolean shift) {

		}
	}

	@Override
	public void render() {

	}

	@Override
	public Color getBGColor() {
		return null;
	}
}
