package roguelike.Components;

import roguelike.Enums.EquipmentSlot;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class Inventory implements Component{

	public ArrayList<Integer> inventory;

	public Inventory(){
		inventory = new ArrayList<>();
	}

	public ArrayList<Integer> get_items_that_fit_slot(EquipmentSlot slot){
		ArrayList<Integer> items = new ArrayList<>();

		for(Integer item : inventory){
			if(entityManager.gc(item, Equippable.class).slots.contains(slot))
				items.add(item);
		}

		return items;
	}
	public void add_item(Integer item){
		inventory.add(item);
	}

	public void remove_item(Integer item){
		inventory.remove(item);
	}
}
