package roguelike.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import static roguelike.engine.Game.*;

public class Game_Screen extends Screen{

    private Game game;
    private Stage stage;
    private SparseLayers display;

    public Game_Screen(Game game_in){
        game = game_in;
        stage = game.stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableSlabFont());
        bgColor = SColor.BLACK;
        display.fillBackground(bgColor);
        stage.addActor(display);
    }

    @Override
    public void render(){
        String test = "We have success!";
        display.put(display.gridWidth / 2 - test.length() / 2, 5, test, Color.YELLOW);
    }
}
