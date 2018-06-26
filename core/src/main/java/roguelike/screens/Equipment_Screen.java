package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import roguelike.engine.Game;
import roguelike.utilities.Roll;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.engine.Game.*;

public class Equipment_Screen extends ScreenAdapter {

	private Game game;
	private Stage stage;
	private SparseLayers display;

	private Color bgColor;

	private SquidInput input;

	private Integer entity;

	public Equipment_Screen(Integer entity, Game game){
		this.game = game;
		this.entity = entity;
		stage = game.stage;
		display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableSlabFont());
		display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
		bgColor = SColor.DB_MIDNIGHT;
		display.fillBackground(bgColor);
	//	stage.addActor(display);
	}

	@Override
	public void show(){
		stage.clear();
		stage.addActor(display);
		input = new SquidInput((key, alt, ctrl, shift) -> {
			switch(key) {
				case SquidInput.ENTER: {
					game.setScreen(game.getGame_screen());
					break;
				}
				default: {
					display.put(Roll.rand(0, gridWidth-4), Roll.rand(0, gridHeight - 1), "Poop", SColor.CW_RICH_BROWN);
					break;
				}
			}
		});

		Gdx.input.setInputProcessor(input);

	}

	@Override
	public void render(float delta) {
		display.put(display.gridWidth / 2, display.gridHeight / 2, "Poop", SColor.CW_RICH_BROWN);
		stage.draw();
		stage.act();
		if(input.hasNext())
			input.next();
	}

	@Override
	public void hide() {
		display.clear();
	}
}
