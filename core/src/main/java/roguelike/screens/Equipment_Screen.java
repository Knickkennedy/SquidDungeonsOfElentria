package roguelike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import lombok.Getter;
import roguelike.Components.Equipment;
import roguelike.Enums.Equipment_Slot;
import roguelike.engine.Game;
import roguelike.utilities.Word;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.SquidInput;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;

@Getter
public class Equipment_Screen extends ScreenAdapter {

	private Game game;

	private SpriteBatch batch;
	private StretchViewport viewport;
	private Stage stage;
	private SparseLayers display;

	private Color bgColor;

	private SquidInput input;

	private Integer entity;
	private String alphabet = "abcdefghijklmnopqrstuvwxyz";

	private Equipment_Slot[] slots;

	private Equip_From_Inventory_Screen equip_from_inventory_screen;

	public Equipment_Screen(Integer entity, Game game){
		this.game = game;
		this.entity = entity;

		batch = new SpriteBatch();
		viewport = new StretchViewport(gridWidth * cellWidth, gridHeight * cellHeight);
		viewport.setScreenBounds(0, 0, gridWidth * cellWidth, gridHeight * cellHeight);
		stage = new Stage(viewport, batch);
		display = new SparseLayers(gridWidth, gridHeight, cellWidth, cellHeight, DefaultResources.getCrispDejaVuFont());
		display.font.tweakHeight(cellHeight *1.1f).initBySize();
		bgColor = SColor.DB_MIDNIGHT;
		display.fillBackground(bgColor);
		slots = Equipment_Slot.values();

		equip_from_inventory_screen = new Equip_From_Inventory_Screen(game, entity, this);
	}

	@Override
	public void show(){
		stage.addActor(display);
		input = new SquidInput((key, alt, ctrl, shift) -> {

			if(alphabet.indexOf(key) > -1 && alphabet.indexOf(key) < slots.length){
				attempt_equip_or_unequip(slots[alphabet.indexOf(key)]); return;
			}

			switch(key) {
				case SquidInput.ESCAPE:
					game.setScreen(game.getGame_screen()); break;
				/*case 'a':
					attempt_equip_or_unequip(slots[0]); break;
				case 'b':
					attempt_equip_or_unequip(slots[1]); break;
				case 'c':
					attempt_equip_or_unequip(slots[2]); break;
				case 'd':
					attempt_equip_or_unequip(slots[3]); break;*/
			}
		});

		Gdx.input.setInputProcessor(input);

	}

	public void attempt_equip_or_unequip(Equipment_Slot slot){
		if(entityManager.gc(entity, Equipment.class).get_slot(slot) == null){
			equip_from_inventory_screen.set_slot(slot);
			game.setScreen(equip_from_inventory_screen);
		}
		else{
			entityManager.gc(entity, Equipment.class).unequip_item(slot, entity);
		}
	}

	public void display_equipment(){

		int i = 5;

		for(int j = 0; j < gridWidth; j++){
			display.put(j, i - 1, '=', SColor.WHITE);
		}

		for(int j = i; j < gridHeight; j++){
			display.put(0, j, '|', SColor.WHITE);
			display.put(gridWidth - 1, j, '|', SColor.WHITE);
		}

		int current_letter = 0;

		for(Equipment_Slot slot : Equipment_Slot.values()){
			String item_name = entityManager.gc(entity, Equipment.class).get_item_name(slot);
			if(item_name.length() > 0){
				item_name = String.format("[%s]", Word.capitalize_all(item_name));
			}
			String slot_name = String.format("[%s]", slot.toString());

			display.put(2, i, String.format("[%s] %-15s: %s", alphabet.charAt(current_letter), slot_name, item_name), SColor.WHITE);
			current_letter++;
			for(int j = 1; j < gridWidth - 1; j++){
				display.put(j, i + 1, '=', SColor.WHITE);
			}
			i = i + 2;
		}
	}

	@Override
	public void render(float delta) {
		display.clear();
		display_equipment();
		stage.draw();
		stage.act();
		if(input.hasNext())
			input.next();
	}

	@Override
	public void hide() {
		display.clear();
	}
}
