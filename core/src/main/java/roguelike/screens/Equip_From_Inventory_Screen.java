package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import lombok.Setter;
import roguelike.Components.Details;
import roguelike.Components.Equipment;
import roguelike.Components.Inventory;
import roguelike.Enums.Equipment_Slot;
import roguelike.engine.Game;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;
import static roguelike.engine.Game.cellHeight;
import static roguelike.engine.Game.cellWidth;

@Getter @Setter
public class Equip_From_Inventory_Screen extends ScreenAdapter {

	private Game game;
	private Integer entity;
	private Equipment_Screen equipment_screen;

	private SpriteBatch batch;
	private StretchViewport viewport;
	private Stage stage;
	private SparseLayers display;
	private Color bgColor;

	private SquidInput input;

	private ArrayList<Integer> items;
	private Equipment_Slot slot;

	private String alphabet = "abcdefghijklmnopqrstuvwxyz";

	public Equip_From_Inventory_Screen(Game game, Integer entity, Equipment_Screen equipment_screen){
		this.game = game;
		this.entity = entity;
		this.equipment_screen = equipment_screen;

		batch = new SpriteBatch();
		viewport = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
		viewport.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);
		stage = new Stage(viewport, batch);
		display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getStretchableSlabFont());
		display.font.tweakWidth(cellWidth + 1).tweakHeight(cellHeight + 1).setSmoothingMultiplier(1.6f).initBySize();
		bgColor = SColor.DB_MIDNIGHT;
		display.fillBackground(bgColor);
	}

	public void set_slot(Equipment_Slot slot){
		this.slot = slot;
		items = new ArrayList<>(entityManager.gc(entity, Inventory.class).get_items_that_fit_slot(slot));
	}

	@Override
	public void show(){
		stage.addActor(display);
		input = new SquidInput((key, alt, ctrl, shift) -> {

			if(alphabet.indexOf(key) > -1 && alphabet.indexOf(key) < items.size()){
				equip_item(items.get(alphabet.indexOf(key)));
				game.setScreen(equipment_screen);
			}

			switch(key) {
				case SquidInput.ESCAPE:
					game.setScreen(game.getGame_screen()); break;
				/*case 'a':
					equip_item(items.get(0));
					game.setScreen(equipment_screen); break;
				case 'b':
					equip_item(items.get(1));
					game.setScreen(equipment_screen); break;
				case 'c':
					equip_item(items.get(2));
					game.setScreen(equipment_screen); break;*/
				default: break;
			}
		});

		Gdx.input.setInputProcessor(input);

	}

	public void equip_item(Integer item){
		entityManager.gc(entity, Equipment.class).equip_item_from_inventory(entity, item, slot);
	}

	@Override
	public void render(float delta) {
		display.clear();

		display_slotted_inventory();

		stage.draw();
		stage.act();
		if(input.hasNext())
			input.next();
	}

	public void display_slotted_inventory(){
		for(int i = 0; i < items.size(); i++){
			Integer item = items.get(i);
			String name = entityManager.gc(item, Details.class).name;
			display.put(2, i + 1, String.format("[%s] [%s]", alphabet.charAt(i), name), SColor.WHITE);
		}
	}

	@Override
	public void hide(){
		display.clear();
	}

}
