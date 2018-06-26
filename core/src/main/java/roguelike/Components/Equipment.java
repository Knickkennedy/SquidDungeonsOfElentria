package roguelike.Components;

import roguelike.Effects.Damage;
import roguelike.Enums.Equipment_Slot;
import roguelike.utilities.Dice;

import java.util.ArrayList;
import java.util.HashMap;

import static roguelike.Generation.World.entityManager;

public class Equipment implements Component{
	public HashMap<Equipment_Slot, Integer> equipment;

	public Equipment(){
		equipment = new HashMap<>();

		for(Equipment_Slot slot : Equipment_Slot.values()){
			equipment.put(slot, null);
		}
	}

	public int[] total_armor(){

		int piercing = 0;
		int slashing = 0;
		int crushing = 0;

		for(Equipment_Slot slot : Equipment_Slot.values()){
			if(equipment.get(slot) != null){
				if(entityManager.gc(equipment.get(slot), Armor.class) != null) {
					piercing += entityManager.gc(equipment.get(slot), Armor.class).piercing;
					slashing += entityManager.gc(equipment.get(slot), Armor.class).slashing;
					crushing += entityManager.gc(equipment.get(slot), Armor.class).crushing;
				}
			}
		}

		return new int[]{piercing, slashing, crushing};
	}

	public ArrayList<Damage> get_base_damage(){
		ArrayList<Damage> damages = new ArrayList<>();
		damages.add(new Damage("crushing", new Dice(1, 3)));
		return damages;
	}

	public ArrayList<Damage> get_left_damage(){
		return entityManager.gc(equipment.get(Equipment_Slot.LEFT_HAND), Offensive_Component.class).damages;
	}

	public ArrayList<Damage> get_right_damage(){
		return entityManager.gc(equipment.get(Equipment_Slot.RIGHT_HAND), Offensive_Component.class).damages;
	}

	public ArrayList<Damage> get_dual_wield_damages(){
		ArrayList<Damage> temp = new ArrayList<>(entityManager.gc(equipment.get(Equipment_Slot.RIGHT_HAND), Offensive_Component.class).damages);
		temp.addAll(entityManager.gc(equipment.get(Equipment_Slot.LEFT_HAND), Offensive_Component.class).damages);

		return temp;
	}

	public ArrayList<Damage> get_melee_damages(){

		if(equipment.get(Equipment_Slot.LEFT_HAND) != null && equipment.get(Equipment_Slot.RIGHT_HAND) != null){

			if(entityManager.gc(equipment.get(Equipment_Slot.LEFT_HAND), Offensive_Component.class) != null
					&& entityManager.gc(equipment.get(Equipment_Slot.RIGHT_HAND), Offensive_Component.class) != null){
				return get_dual_wield_damages();
			}
			else if(entityManager.gc(equipment.get(Equipment_Slot.RIGHT_HAND), Offensive_Component.class) != null
					&& entityManager.gc(equipment.get(Equipment_Slot.LEFT_HAND), Offensive_Component.class) == null){
				return get_right_damage();
			}
			else if(entityManager.gc(equipment.get(Equipment_Slot.LEFT_HAND), Offensive_Component.class) != null
					&& entityManager.gc(equipment.get(Equipment_Slot.RIGHT_HAND), Offensive_Component.class) == null){
				return get_left_damage();
			}
			else
				return get_base_damage();

		}
		else if(equipment.get(Equipment_Slot.LEFT_HAND) != null && equipment.get(Equipment_Slot.RIGHT_HAND) == null){
			if(entityManager.gc(equipment.get(Equipment_Slot.LEFT_HAND), Offensive_Component.class) != null){
				return get_left_damage();
			}
			else
				return get_base_damage();
		}
		else if(equipment.get(Equipment_Slot.RIGHT_HAND) != null && equipment.get(Equipment_Slot.LEFT_HAND) == null){
			if(entityManager.gc(equipment.get(Equipment_Slot.RIGHT_HAND), Offensive_Component.class) != null){
				return get_right_damage();
			}
			else
				return get_base_damage();
		}
		else
			return get_base_damage();
	}

	public void equip_item(Integer owner, Integer item, Equipment_Slot slot){

		if(entityManager.gc(item, Equippable.class).slots.contains(slot) && equipment.get(slot) == null){
			equipment.put(slot, item);
		}
		else if(entityManager.gc(item, Equippable.class).slots.contains(slot) && equipment.get(slot) != null){
			entityManager.gc(owner, Inventory.class).add_item(equipment.remove(slot));
		}
		else{
			System.out.println("That doesn't go there.");
		}
	}
}
