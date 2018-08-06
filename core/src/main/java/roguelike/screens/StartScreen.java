package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.engine.Game.*;

public class StartScreen extends ScreenAdapter {

    private Game game;
    private Stage stage;
    private SparseLayers display;
    private SpriteBatch batch;
    private StretchViewport startViewPort;

    private Color bgColor;

    private SquidInput input;

    public StartScreen(Game game_in){
        game = game_in;
        batch = new SpriteBatch();
        startViewPort = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
        startViewPort.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);
        stage = new Stage(startViewPort, batch);
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
        display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
        bgColor = SColor.BLACK;
        display.fillBackground(bgColor);

    }

    @Override
    public void show(){
	    stage.addActor(display);
	    input = new SquidInput((key, alt, ctrl, shift) -> {

		    switch(key) {
                case SquidInput.ENTER: {
                    game.setScreen(game.makeNewGameScreen());
                    break;
                }
            }
	    });

	    Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta){

        String title = "Dungeons of Elentria";
        String enter = "Press [Enter] to start a new game";
        display.put(display.gridWidth / 2 - title.length() / 2, 5, title, Color.WHITE);
        display.put(display.gridWidth / 2 - enter.length() / 2, gridHeight - 5, enter, Color.WHITE);

        if(input.hasNext()){
            input.next();
        }
        stage.act();
        stage.draw();
    }

	@Override
	public void hide(){

	}

    @Override
    public void resume() {
//        if(input != null)
//            input.setIgnoreInput(true);
    }
}
