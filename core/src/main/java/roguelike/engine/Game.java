package roguelike.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import lombok.Setter;
import roguelike.screens.Game_Screen;
import roguelike.screens.Start_Screen;

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

	    setScreen(new Start_Screen(this));
    }
    @Override
    public void render () {
        // standard clear the background routine for libGDX
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Screen screen = getScreen();
        if(screen != null) 
            screen.render(Gdx.graphics.getDeltaTime());
//        stage.draw();
//        stage.act();
    }

    @Override
	public void dispose(){
    	batch.dispose();
    	stage.dispose();
    }
}
