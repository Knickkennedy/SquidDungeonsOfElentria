package roguelike.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import static roguelike.engine.Game.*;

public class Start_Screen {

    private Stage stage;
    private SparseLayers display;

    public Start_Screen(Stage stage){
        this.stage = stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableSlabFont());
    }

    public void render(){
        String title = "Dungeons of Elentria";
        display.put(display.gridWidth / 2 - title.length() / 2, 5, title, Color.WHITE);
        stage.draw();
    }
}
