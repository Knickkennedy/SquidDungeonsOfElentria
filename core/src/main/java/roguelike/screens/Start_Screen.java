package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.engine.Game.*;

public class Start_Screen extends Screen{

    private Game game;
    private Stage stage;
    private SparseLayers display;
    private SquidInput input;

    public Start_Screen(Game game_in){
        game = game_in;
        stage = game.stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableSlabFont());
        bgColor = SColor.BLACK;
        display.fillBackground(bgColor);
        stage.addActor(display);
        input = new SquidInput(new SquidInput.KeyHandler() {
            @Override
            public void handle(char key, boolean alt, boolean ctrl, boolean shift) {

                switch(key){
                    case SquidInput.ENTER:
                    {
                        game.setCurrent_screen(new Game_Screen(game)); break;
                    }
                }
            }
        });

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, input));
    }

    @Override
    public void render(){
        String title = "Dungeons of Elentria";
        String enter = "Press [Enter] to start a new game";
        display.put(display.gridWidth / 2 - title.length() / 2, 5, title, Color.WHITE);
        display.put(display.gridWidth / 2 - enter.length() / 2, gridHeight - 5, enter, Color.WHITE);

        if(input.hasNext()){
            input.next();
        }
        stage.draw();
    }
}
