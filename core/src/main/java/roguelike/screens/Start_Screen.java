package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.engine.Game.*;

public class Start_Screen extends ScreenAdapter {

    private Game game;
    private Stage stage;
    private SparseLayers display;

    private Color bgColor;

    private SquidInput input;

    public Start_Screen(Game game_in){
        game = game_in;
        stage = game.stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
        display.font.tweakHeight(cellHeight *1.1f).initBySize();
        bgColor = SColor.DB_MIDNIGHT;
        display.fillBackground(bgColor);

    }

    @Override
    public void show(){
	    stage.addActor(display);
	    input = new SquidInput((key, alt, ctrl, shift) -> {

		    switch(key) {
                case SquidInput.ENTER: {
	                game.setGame_screen(new Game_Screen(game));
                    game.setScreen(game.getGame_screen());
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
        stage.draw();
        stage.act();
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
