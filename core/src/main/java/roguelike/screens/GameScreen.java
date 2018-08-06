package roguelike.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.Generation.Factory;
import roguelike.Generation.World;
import roguelike.engine.Game;
import roguelike.engine.MessageLog;
import roguelike.utilities.Colors;
import squidpony.squidgrid.gui.gdx.*;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.Set;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;

public class GameScreen extends ScreenAdapter {

    private Game game;
    public Stage stage;
    public StretchViewport gameViewPort;
    public SpriteBatch batch;
    public SparseLayers display;

    private Color bgColor;

    private World world;

    private int map_height_start;
    private int map_height_end;

    public GameScreen(Game game_in){
        game = game_in;
        batch = new SpriteBatch();
        gameViewPort = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
        gameViewPort.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);
        stage = new Stage(gameViewPort, batch);
        Factory.getInstance().setGame(game_in);
	    display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());

    }

    @Override
    public void show(){
        stage.clear();
        display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
        bgColor = SColor.BLACK;
        display.fillBackground(bgColor);
        map_height_start = message_buffer;
        map_height_end = gridHeight - statistics_height + message_buffer;
        if(world != null)
            world.reload();
        else
            world = new World(gridWidth, gridHeight - statistics_height, display);
        stage.addActor(display);
	    /*GreasedRegion greasedRegion = new GreasedRegion(gridWidth, gridHeight);
	    greasedRegion.allOn();
	    display.addAction(new PanelEffect.ProjectileEffect(display, 5f, greasedRegion, Coord.get(10, 10), Coord.get(10, 15), '/', SColor.BROWN));
    */
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

	public void render_messages(){
    	String[] temp = new String[] {"", ""};
    	for(String message : MessageLog.getInstance().messages){

    		if(temp[0].concat(message).length() < gridWidth){
			    temp[0] = temp[0].concat(" ");
			    temp[0] = temp[0].concat(message);
		    }
		    else if(temp[1].concat(message).length() >= gridWidth){
			    MessageLog.getInstance().messages.clear();
			    MessageLog.getInstance().messages.add(message);
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

    public void render_map(){
        double[][] fov = entityManager.gc(world.getPlayer(), Vision.class).getFov();
        for(int i  = 0; i < gridWidth; i++){
            for(int j = map_height_start; j < map_height_end; j++){
                Sprite sprite = world.getCurrent_map().getTileAt(i, j - message_buffer).sprite;
                if(fov[i][j - message_buffer] > 0)
                    display.putWithConsistentLight(i, j, sprite.character, sprite.foregroundColor, bgColor, SColor.CW_PALE_YELLOW, (float)(fov[i][j - message_buffer]));
                else
                    display.put(i, j, sprite.character, sprite.foregroundColor, bgColor);
            }
        }
    }

    public void render_statistics(){
	    Statistics temp = entityManager.gc(world.getPlayer(), Statistics.class);
	    String health = String.format("HP:%s/%s", temp.health.current_value, temp.health.maximum);
	    String first = String.format("Str:%s Int:%s Will:%s", temp.strength, temp.intelligence, temp.willpower);
	    String second = String.format("Con:%s Dex:%s Char:%s", temp.constitution, temp.dexterity, temp.charisma);

	    display.put(1, map_height_end, health, Colors.getColor("green"));
    	display.put(gridWidth / 2 - first.length() / 2, map_height_end, first, Colors.getColor("white"));
    	display.put(gridWidth / 2 - second.length() / 2, map_height_end + 1, second, Colors.getColor("white"));

    	int[] armor = entityManager.gc(world.getPlayer(), Equipment.class).total_armor();
    	String armor_string = String.format("Pierce:%d Slash:%d Crush:%d", armor[0], armor[1], armor[2]);

	    ArrayList<Damage> damage_list = entityManager.gc(world.getPlayer(), Equipment.class).get_melee_damages();
	    StringBuilder melee_damage = new StringBuilder();
	    for(Damage damage : damage_list){
	    	melee_damage.append(String.format("Type: %s %s", damage.type, damage.dice));
	    }

	    String melee_string = melee_damage.toString();

    	display.put(gridWidth - armor_string.length() - 1, map_height_end, armor_string, Colors.getColor("gray"));
	    display.put(gridWidth - melee_string.length() - 1, map_height_end + 1, melee_string, Colors.getColor("gray"));
    }

    public void render_entities(){

    	if(display.hasActiveAnimations())
    		return;
        Set <Integer> entities = entityManager.getAllEntitiesPossessingComponent(Active.class);
        for(Integer entity : entities){
            place_entity(entity, entityManager.gc(entity, Position.class).location);
        }
    }

    private void place_entity(Integer entity, Coord point){
        display.put(point.x, point.y + message_buffer, entityManager.gc(entity, Sprite.class).character, entityManager.gc(entity, Sprite.class).foregroundColor);
        Sprite sprite = entityManager.gc(entity, Sprite.class);
        Position position = entityManager.gc(entity, Position.class);
        if(position == null)
        	return;
        if(sprite != null && position.map.equals(world.getCurrent_map()))
            sprite.makeGlyph(display, point.x, point.y + message_buffer);
    }

    @Override
	public void hide(){
    }
}
