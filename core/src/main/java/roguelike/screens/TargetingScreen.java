package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import roguelike.Actions.Animations.Animation;
import roguelike.Actions.RangedAttack;
import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.Generation.World;
import roguelike.engine.Game;
import roguelike.utilities.Colors;
import roguelike.utilities.Point;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidmath.Bresenham;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;
import static roguelike.engine.Game.cellHeight;
import static roguelike.engine.Game.cellWidth;

public class TargetingScreen extends ScreenAdapter {

	private Game game;
	private World world;
	private Integer entity;
	private int range;
	private Coord start;
	private Coord end;
	private SquidInput input;

	private SpriteBatch batch;
	private StretchViewport viewport;
	private Stage stage;
	private SparseLayers display;
	private List<Animation> animations;

	private Queue<Coord> line;

	private Color bgColor;

	public TargetingScreen(Game game, World world, Integer entity, List<Animation> animations){
		this.game = game;
		this.world = world;
		this.entity = entity;
		this.start = entityManager.gc(entity, Position.class).location;
		this.end = entityManager.gc(entity, Position.class).location;

		batch = new SpriteBatch();
		viewport = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
		viewport.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);
		stage = new Stage(viewport, batch);
		display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
		display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
		bgColor = SColor.BLACK;
		display.fillBackground(bgColor);
		this.animations = animations;
	}

	@Override
	public void show(){
		stage.addActor(display);

		Equipment equipment = entityManager.gc(entity, Equipment.class);
		this.range = equipment.getRange();

		start = entityManager.gc(entity, Position.class).location;
		end = entityManager.gc(entity, Position.class).location;
		input = new SquidInput((key, alt, ctrl, shift) -> {
			switch(key) {
				case SquidInput.DOWN_LEFT_ARROW:
					tryMove(Point.SOUTH_WEST); break;
				case SquidInput.DOWN_ARROW:
					tryMove(Point.SOUTH); break;
				case SquidInput.DOWN_RIGHT_ARROW:
					tryMove(Point.SOUTH_EAST); break;
				case SquidInput.LEFT_ARROW:
					tryMove(Point.WEST); break;
				case SquidInput.CENTER_ARROW:
					tryMove(Point.WAIT); break;
				case SquidInput.RIGHT_ARROW:
					tryMove(Point.EAST); break;
				case SquidInput.UP_LEFT_ARROW:
					tryMove(Point.NORTH_WEST); break;
				case SquidInput.UP_ARROW:
					tryMove(Point.NORTH); break;
				case SquidInput.UP_RIGHT_ARROW:
					tryMove(Point.NORTH_EAST); break;
				case SquidInput.ENTER:
					entityManager.gc(entity, ActionComponent.class).setAction(new RangedAttack(entity, line, game.getGame_screen().display, animations));
					if(!display.hasActiveAnimations()) {
						game.setScreen(game.getGame_screen());
					}
					break;
				case SquidInput.ESCAPE:
					game.setScreen(game.getGame_screen()); break;
			}
		});

		Gdx.input.setInputProcessor(input);
	}

	public void tryMove(Coord direction){
		end = end.add(direction);
		line = Bresenham.line2D(start, end);

		if(line.size() > range)
			end = end.subtract(direction);
	}

	@Override
	public void render(float delta) {
		display.clear();


		renderMap();
		renderStatistics();
		renderEntities();
		renderTargetLine();
		if(input.hasNext())
			input.next();

		stage.act();
		stage.draw();
	}

	private void renderMap(){
		double[][] fov = entityManager.gc(entity, Vision.class).getFov();
		for(int i  = 0; i < world.getMap_width(); i++){
			for(int j = 0; j < world.getMap_height(); j++){
				Sprite sprite = world.getCurrent_map().getTileAt(i, j).sprite;
				if(fov[i][j] > 0)
					display.putWithConsistentLight(i, j + message_buffer, sprite.character, sprite.foregroundColor, Color.BLACK, SColor.CW_PALE_YELLOW, (float)(fov[i][j]));
				else
					display.put(i, j + message_buffer, sprite.character, sprite.foregroundColor, bgColor);
			}
		}
	}

	public void renderStatistics(){
		Statistics temp = entityManager.gc(world.getPlayer(), Statistics.class);
		String health = String.format("HP:%s/%s", temp.health.current_value, temp.health.maximum);
		String first = String.format("Str:%s Int:%s Will:%s", temp.strength, temp.intelligence, temp.willpower);
		String second = String.format("Con:%s Dex:%s Char:%s", temp.constitution, temp.dexterity, temp.charisma);

		display.put(1, world.getMap_height() + message_buffer, health, Colors.getColor("green"));
		display.put(gridWidth / 2 - first.length() / 2, world.getMap_height() + message_buffer, first, Colors.getColor("white"));
		display.put(gridWidth / 2 - second.length() / 2, world.getMap_height() + message_buffer + 1, second, Colors.getColor("white"));

		int[] armor = entityManager.gc(world.getPlayer(), Equipment.class).total_armor();
		String armor_string = String.format("Pierce:%d Slash:%d Crush:%d", armor[0], armor[1], armor[2]);

		ArrayList<Damage> damage_list = entityManager.gc(world.getPlayer(), Equipment.class).get_melee_damages();
		StringBuilder melee_damage = new StringBuilder();
		for(Damage damage : damage_list){
			melee_damage.append(String.format("Type: %s %s", damage.type, damage.dice));
		}

		String melee_string = melee_damage.toString();

		display.put(gridWidth - armor_string.length() - 1, world.getMap_height() + message_buffer, armor_string, Colors.getColor("gray"));
		display.put(gridWidth - melee_string.length() - 1, world.getMap_height() + 1 + message_buffer, melee_string, Colors.getColor("gray"));
	}

	private void renderEntities(){

		if(display.hasActiveAnimations())
			return;
		Set<Integer> entities = entityManager.getAllEntitiesPossessingComponent(Active.class);
		for(Integer entity : entities){
			placeEntity(entity, entityManager.gc(entity, Position.class).location);
		}
	}

	private void placeEntity(Integer entity, Coord location){
		Sprite sprite = entityManager.gc(entity, Sprite.class);
		display.put(location.x, location.y + message_buffer, sprite.character, sprite.foregroundColor);
	}

	private void renderTargetLine(){

		if(line == null || line.isEmpty())
			return;

		Position position = entityManager.gc(entity, Position.class);
		for(Coord location : line){

			if(position.location != location) {
				if(!position.map.isSolid(location.x, location.y)) {
					display.clear(location.x, location.y + message_buffer);
					display.put(location.x, location.y + message_buffer, '/', SColor.CHESTNUT_LEATHER_BROWN, SColor.CW_FADED_GOLD);
				}
				else{
					display.clear(location.x, location.y + message_buffer);
					display.put(location.x, location.y + message_buffer, 'X', SColor.RED, SColor.CW_FADED_GOLD);
				}

				if(position.map.entityAt(location) != null){
					display.clear(location.x, location.y + message_buffer);
					display.put(location.x, location.y + message_buffer, '!', SColor.DARK_GREEN, SColor.CW_FADED_GOLD);
				}
			}
		}
	}

	@Override
	public void hide() {
		display.clear();
	}
}
