package roguelike.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import roguelike.engine.Game;
import roguelike.engine.Map;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import static roguelike.engine.Game.*;

public class Game_Screen extends Screen{

    private Game game;
    private Stage stage;
    private SparseLayers display;
    private Map map;

    public Game_Screen(Game game_in){
        game = game_in;
        stage = game.stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableSlabFont());
        bgColor = SColor.BLACK;
        display.fillBackground(bgColor);
        map = new Map(gridWidth, gridHeight);
        map.buildStandardLevel();
        stage.addActor(display);
    }

    @Override
    public void render(){
        render_map();
    }

    public void render_map(){
        for(int i  = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                display.put(i, j, map.getTileAt(i, j).getSprite().character, map.getTileAt(i, j).getSprite().foregroundColor);
            }
        }
    }
}
