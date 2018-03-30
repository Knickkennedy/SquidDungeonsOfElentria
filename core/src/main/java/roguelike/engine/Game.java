package roguelike.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import lombok.Setter;
import roguelike.screens.Screen;
import roguelike.screens.Start_Screen;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.RNG;

/**
 * This is a small, not-overly-simple demo that presents some important features of SquidLib and shows a faster,
 * cleaner, and more recently-introduced way of displaying the map and other text. Features include dungeon map
 * generation, field of view, pathfinding (to the mouse position), simplex noise (used for a flickering torch effect),
 * language generation/ciphering, color manipulation, and ever-present random number generation (with a seed).
 * You can increase the size of the map on most target platforms (but GWT struggles with large... anything) by
 * changing gridHeight and gridWidth to affect the visible area or bigWidth and bigHeight to adjust the size of the
 * dungeon you can move through, with the camera following your '@' symbol.
 */
@Getter @Setter
public class Game extends ApplicationAdapter {
    SpriteBatch batch;

    private RNG rng;

    public static final int gridWidth = 112;
    public static final int gridHeight = 32;


    public static final int cellWidth = 10;
    public static final int cellHeight = 20;

    public Stage stage;
    private Screen current_screen;

    @Override
    public void create () {
        rng = new RNG("SquidLib!");

        batch = new SpriteBatch();

        StretchViewport mainViewport = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
        mainViewport.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);

        stage = new Stage(mainViewport, batch);

        current_screen = new Start_Screen(this);
    }

    @Override
    public void render () {
        // standard clear the background routine for libGDX
        Gdx.gl.glClearColor(current_screen.bgColor.r / 255.0f, current_screen.bgColor.g / 255.0f, current_screen.bgColor.b / 255.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        current_screen.render();

        stage.draw();
        stage.act();
    }
}
