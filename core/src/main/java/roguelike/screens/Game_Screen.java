package roguelike.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.json.simple.parser.ParseException;
import roguelike.Components.Active;
import roguelike.Components.Position;
import roguelike.Components.Sprite;
import roguelike.Components.Vision;
import roguelike.Generation.World;
import roguelike.engine.Game;
import roguelike.utilities.Point;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;

import java.io.IOException;
import java.util.Set;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;

public class Game_Screen extends Screen {

    private Game game;
    private Stage stage;
    private SparseLayers display;
    private World world;

    Game_Screen(Game game_in) throws IOException, ParseException{
        game = game_in;
        stage = game.stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
        display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableTypewriterFont());
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableCodeFont());
        bgColor = SColor.DB_MIDNIGHT;
        display.fillBackground(bgColor);
		world = new World(gridWidth, gridHeight - statistics_height);
        stage.addActor(display);
    }

    @Override
    public void render(){

        world.update();
	    render_map();
        render_entities();

        stage.draw();

    }

    private void render_map(){
        double[][] fov = entityManager.gc(world.getPlayer(), Vision.class).getFov();
        for(int i  = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight - statistics_height; j++){
                Sprite sprite = world.getCurrent_map().getTileAt(i, j).getSprite();
                if(fov[i][j] > 0) 
                    display.putWithConsistentLight(i, j, sprite.character, sprite.foregroundColor, Color.BLACK, SColor.CREAM, (float)(fov[i][j]));
                else
                    display.put(i, j, sprite.character, sprite.foregroundColor, bgColor);
            }
        }
    }

    private void render_entities(){
        Set <Integer> entities = entityManager.getAllEntitiesPossessingComponent(Active.class);
        for(Integer entity : entities){
            place_entity(entity, entityManager.gc(entity, Position.class).getLocation());
        }
    }


    private void place_entity(Integer entity, Point point){
        display.put(point.x, point.y, entityManager.gc(entity, Sprite.class).character, entityManager.gc(entity, Sprite.class).foregroundColor);
    }
}
