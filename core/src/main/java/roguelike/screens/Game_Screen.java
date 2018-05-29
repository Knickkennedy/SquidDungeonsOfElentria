package roguelike.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.json.simple.parser.ParseException;
import roguelike.Components.Active;
import roguelike.Components.Position;
import roguelike.Components.Sprite;
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
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableDejaVuFont());
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableTypewriterFont());
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableCodeFont());
        bgColor = SColor.BLACK;
        display.fillBackground(bgColor);
		world = new World(gridWidth, gridHeight - statistics_height);
        stage.addActor(display);
    }

    @Override
    public void render(){

        world.update();
        render_map();
        render_entities();

    }

    private void render_map(){
        for(int i  = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight - statistics_height; j++){
                display.put(i, j, world.getCurrent_map().getTileAt(i, j).getSprite().character, world.getCurrent_map().getTileAt(i, j).getSprite().foregroundColor);
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
