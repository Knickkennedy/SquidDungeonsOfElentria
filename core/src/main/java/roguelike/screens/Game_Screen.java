package roguelike.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.json.simple.parser.ParseException;
import roguelike.Components.*;
import roguelike.Generation.World;
import roguelike.engine.Game;
import roguelike.utilities.Colors;
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

    private int map_height_start;
    private int map_height_end;

    Game_Screen(Game game_in) throws IOException, ParseException{
        game = game_in;
        stage = game.stage;
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
        display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableTypewriterFont());
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableCodeFont());
        bgColor = SColor.DB_MIDNIGHT;
        display.fillBackground(bgColor);
        map_height_start = message_buffer;
        map_height_end = gridHeight - statistics_height + message_buffer;
		world = new World(gridWidth, gridHeight - statistics_height);
        stage.addActor(display);
    }

    @Override
    public void render(){

        world.update();
	    render_map();
        render_entities();
        render_statistics();

        stage.draw();

    }

    private void render_map(){
        double[][] fov = entityManager.gc(world.getPlayer(), Vision.class).getFov();
        for(int i  = 0; i < gridWidth; i++){
            for(int j = map_height_start; j < map_height_end; j++){
                Sprite sprite = world.getCurrent_map().getTileAt(i, j - message_buffer).sprite;
                if(fov[i][j - message_buffer] > 0)
                    display.putWithConsistentLight(i, j, sprite.character, sprite.foregroundColor, Color.BLACK, SColor.CW_PALE_YELLOW, (float)(fov[i][j - message_buffer]));
                else
                    display.put(i, j, sprite.character, sprite.foregroundColor, bgColor);
            }
        }
    }

    private void render_statistics(){
	    Statistics temp = entityManager.gc(world.getPlayer(), Statistics.class);
	    String health = String.format("HP:%s/%s", temp.health, temp.health.maximum);
	    String first = String.format("Str:%s Int:%s Will:%s", temp.strength, temp.intelligence, temp.willpower);
	    String second = String.format("Con:%s Dex:%s Char:%s", temp.constitution, temp.dexterity, temp.charisma);

	    display.put(1, map_height_end, health, Colors.getColor("green"));
    	display.put(gridWidth / 2 - first.length() / 2, map_height_end, first, Colors.getColor("white"));
    	display.put(gridWidth / 2 - second.length() / 2, map_height_end + 1, second, Colors.getColor("white"));

    	int[] armor = entityManager.gc(world.getPlayer(), Equipment.class).total_armor();
    	String armor_string = String.format("Pierce:%d Slash:%d Crush:%d", armor[0], armor[1], armor[2]);

    	display.put(gridWidth - armor_string.length() - 1, map_height_end, armor_string, Colors.getColor("gray"));
    }

    private void render_entities(){
        Set <Integer> entities = entityManager.getAllEntitiesPossessingComponent(Active.class);
        for(Integer entity : entities){
            place_entity(entity, entityManager.gc(entity, Position.class).location);
        }
    }

    private void place_entity(Integer entity, Point point){
        display.put(point.x, point.y + message_buffer, entityManager.gc(entity, Sprite.class).character, entityManager.gc(entity, Sprite.class).foregroundColor);
    }
}
