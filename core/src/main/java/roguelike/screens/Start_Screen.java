package roguelike.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import static roguelike.engine.Game.*;

public class Start_Screen extends Screen{

    private Game game;
    private Stage stage;
    private SparseLayers display;

    public Start_Screen(Game game_in){
        game = game_in;
        stage = game.stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableSlabFont());
        bgColor = SColor.BLACK;
        display.fillBackground(bgColor);
        stage.addActor(display);
    }

    @Override
    public void render(){
        String title = "Dungeons of Elentria";
        String enter = "Press [Enter] to start a new game";
        display.put(display.gridWidth / 2 - title.length() / 2, 5, title, Color.WHITE);
        display.put(display.gridWidth / 2 - enter.length() / 2, gridHeight - 5, enter, Color.WHITE);
        stage.draw();
    }
}
