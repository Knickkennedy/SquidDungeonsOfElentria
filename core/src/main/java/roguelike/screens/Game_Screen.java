package roguelike.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.Generation.Factory;
import roguelike.Generation.World;
import roguelike.engine.Game;
import roguelike.engine.Message_Log;
import roguelike.utilities.Colors;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.TextCellFactory;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.Set;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;

public class Game_Screen extends ScreenAdapter {

    private Game game;
    private Stage stage;
    public SparseLayers display;

    private Color bgColor;

    private World world;

    private int map_height_start;
    private int map_height_end;

    public Game_Screen(Game game_in){
        game = game_in;
        Factory.getInstance().setGame(game_in);
    }

    @Override
    public void show(){
        stage = game.stage;
        stage.clear();
        display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
        display.font.tweakHeight(cellHeight *1.1f).initBySize();
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableTypewriterFont());
        //display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableCodeFont());
        bgColor = SColor.DB_MIDNIGHT;
        display.fillBackground(bgColor);
        entityManager.display = display;
        map_height_start = message_buffer;
        map_height_end = gridHeight - statistics_height + message_buffer;
        if(world != null)
            world.reload();
        else
            world = new World(gridWidth, gridHeight - statistics_height);
        stage.addActor(display);

    }

    @Override
    public void render(float delta){
        display.clear();
        world.update();
	    render_map();
        render_entities();
        render_statistics();
        render_messages();

        stage.act();
        stage.draw();
    }

	private void render_messages(){
    	String[] temp = new String[] {"", ""};
    	for(String message : Message_Log.getInstance().messages){

    		if(temp[0].concat(message).length() < gridWidth){
			    temp[0] = temp[0].concat(" ");
			    temp[0] = temp[0].concat(message);
		    }
		    else if(temp[1].concat(message).length() >= gridWidth){
			    Message_Log.getInstance().messages.clear();
			    Message_Log.getInstance().messages.add(message);
			    break;
		    }
		    else{
			    temp[1] = temp[1].concat(" ");
			    temp[1] = temp[1].concat(message);

		    }

	    }

	    display.put(0, 0, temp[0], Colors.getColor("gray"));
    	display.put(0, 1, temp[1], Colors.getColor("gray"));

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
	    int x = 1, y = map_height_end;
        SColor green = Colors.getColor("green");
        SColor white = Colors.getColor("white");
        SColor gray = Colors.getColor("gray");
        SColor black = SColor.BLACK;
	    String health = "HP:" + temp.health.current_value + "/" + temp.health.maximum; 
                //String.format("HP:%d/%d", temp.health.current_value, temp.health.maximum);
	    String first = "Str:"+ temp.strength.current_value+" Int:"+ temp.intelligence.current_value +" Will:"+ temp.willpower.current_value;
                //String.format("Str:%d Int:%d Will:%d", temp.strength.current_value, temp.intelligence.current_value, temp.willpower.current_value);
	    String second = "Con:"+ temp.constitution.current_value+" Dex:"+ temp.dexterity.current_value +" Char:"+ temp.charisma.current_value;
                //String.format("Con:%d Dex:%d Char:%d", temp.constitution.current_value, temp.dexterity.current_value, temp.charisma.current_value);

	    display.put(1, map_height_end, health, green, black);
    	display.put(gridWidth / 2 - first.length() / 2, map_height_end, first, white, black);
    	display.put(gridWidth / 2 - second.length() / 2, map_height_end + 1, second, white, black);

    	int[] armor = entityManager.gc(world.getPlayer(), Equipment.class).total_armor();
    	String armor_string = "Pierce:" + armor[0] + " Slash:" + armor[1] + " Crush:" + armor[2]; 
                //String.format("Pierce:%d Slash:%d Crush:%d", armor[0], armor[1], armor[2]);

	    ArrayList<Damage> damage_list = entityManager.gc(world.getPlayer(), Equipment.class).get_melee_damages();
	    StringBuilder melee_damage = new StringBuilder();
        for (int i = 0; i < damage_list.size();) {
            Damage damage = damage_list.get(i);
	    	melee_damage.append("Type: ").append(damage.type).append(' ').append(damage.dice);
	    	if(++i < damage_list.size()) // if we aren't the last item
	    	    melee_damage.append(", ");
	    }

	    String melee_string = melee_damage.toString();

    	display.put(gridWidth - armor_string.length() - 1, map_height_end, armor_string, gray, black);
	    display.put(gridWidth - melee_string.length() - 1, map_height_end + 1, melee_string, gray, black);
    }

    private void render_entities(){
        if(display.hasActiveAnimations())
            return;
        Set <Integer> entities = entityManager.getAllEntitiesPossessingComponent(Active.class);
        for(Integer entity : entities){
            place_entity(entity, entityManager.gc(entity, Position.class).location);
        }
    }

    private void place_entity(Integer entity, Coord point){
        Sprite sprite = entityManager.gc(entity, Sprite.class);
        sprite.makeGlyph(display, point.x, point.y + message_buffer);
    }

    @Override
	public void hide(){
    }
}
