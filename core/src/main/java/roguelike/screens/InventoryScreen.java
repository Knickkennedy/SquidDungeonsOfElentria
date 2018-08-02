package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import roguelike.Components.Details;
import roguelike.Components.Inventory;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;
import static roguelike.engine.Game.cellHeight;
import static roguelike.engine.Game.cellWidth;

public class InventoryScreen extends ScreenAdapter {

	private Integer entity;
	private Game game;

	private SpriteBatch batch;
	private StretchViewport viewport;
	private Stage stage;
	private SparseLayers display;

	private Color bgColor;

	private SquidInput input;

	public InventoryScreen(Integer entity, Game game){
		this.entity = entity;
		this.game = game;

		batch = new SpriteBatch();
		viewport = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
		viewport.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);
		stage = new Stage(viewport, batch);
		display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
		display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
		bgColor = SColor.DB_MIDNIGHT;
		display.fillBackground(bgColor);
	}

	@Override
	public void show(){
		stage.addActor(display);
		input = new SquidInput((key, alt, ctrl, shift) -> {
			switch(key) {
				case SquidInput.ESCAPE:
					game.setScreen(game.getGame_screen()); break;
			}
		});

		Gdx.input.setInputProcessor(input);

	}

	@Override
	public void render(float delta) {
		display.clear();

		display_inventory();

		stage.draw();
		stage.act();
		if(input.hasNext())
			input.next();
	}

	public void display_inventory(){
		for(int i = 0; i < entityManager.gc(entity, Inventory.class).inventory.size(); i++){
			Integer item = entityManager.gc(entity, Inventory.class).inventory.get(i);
			String name = entityManager.gc(item, Details.class).name;
			display.put(1, i + 1, name, SColor.WHITE);
		}
	}

	@Override
	public void hide(){
		display.clear();
	}

}
