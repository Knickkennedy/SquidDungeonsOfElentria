package roguelike.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.parser.ParseException;
import roguelike.screens.Game_Screen;
import roguelike.screens.Screen;
import roguelike.screens.Start_Screen;

import java.io.IOException;

@Getter @Setter
public class Game extends com.badlogic.gdx.Game {
    SpriteBatch batch;

    public static final int gridWidth = 112;
    public static final int gridHeight = 32;
    public static final int statistics_height = 4;
    public static final int message_buffer = 2;


    public static final int cellWidth = 10;
    public static final int cellHeight = 20;

    public Stage stage;
    private ScreenAdapter game_screen;

    @Override
    public void create () {

        batch = new SpriteBatch();

        StretchViewport mainViewport = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
        mainViewport.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);

        stage = new Stage(mainViewport, batch);

	    try {
		    game_screen = new Game_Screen(this);
	    } catch (IOException | ParseException e) {
		    e.printStackTrace();
	    }


	    setScreen(new Start_Screen(this));
    }
    /*@Override
    public void render () {
        // standard clear the background routine for libGDX
        Gdx.gl.glClearColor(current_screen.getBGColor().r / 255.0f, current_screen.getBGColor().g / 255.0f, current_screen.getBGColor().b / 255.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        current_screen.render();

        stage.draw();
        stage.act();

    }*/

    @Override
	public void dispose(){
    	batch.dispose();
    	stage.dispose();
    }
}
