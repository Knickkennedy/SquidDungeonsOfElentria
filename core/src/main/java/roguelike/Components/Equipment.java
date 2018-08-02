package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Effects.Damage;
import roguelike.Enums.EquipmentSlot;
import roguelike.Generation.Factory;
import roguelike.utilities.Dice;

import java.util.ArrayList;
import java.util.HashMap;

import static roguelike.Generation.World.entityManager;

public class Equipment implements Component{
	public HashMap<EquipmentSlot, Integer> equipment;

	public Equipment(){
		equipment = new HashMap<>();

		for(EquipmentSlot slot : EquipmentSlot.values()){
			equipment.put(slot, null);
		}
	}

	public Equipment(JSONObject object){

		equipment = new HashMap<>();

		for(Object o : object.keySet()){

			switch (o.toString()){
				case "head": equipment.put(EquipmentSlot.HEAD, Factory.getInstance().create_new_item((String)object.get(o.toString()))); break;
				case "chest": equipment.put(EquipmentSlot.CHEST, Factory.getInstance().create_new_item((String)object.get(o.toString()))); break;
				case "left hand": equipment.put(EquipmentSlot.LEFT_HAND, Factory.getInstance().create_new_item((String)object.get(o.toString()))); break;
				case "right hand": equipment.put(EquipmentSlot.RIGHT_HAND, Factory.getInstance().create_new_item((String)object.get(o.toString()))); break;
			}
		}
	}

	public String get_item_name(EquipmentSlot slot){
		if(equipment.get(slot) == null){
			return "";
		}
		else{
			return entityManager.gc(equipment.get(slot), Details.class).name;
		}
	}

	public Integer get_slot(EquipmentSlot slot){
		return equipment.get(slot);
	}

	public int[] total_armor(){

		int piercing = 0;
		int slashing = 0;
		int crushing = 0;

		for(EquipmentSlot slot : EquipmentSlot.values()){
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

	public int get_resistance_from_type(String type){

		switch (type){
			case "piercing": return get_piercing_resistance();
			case "slashing": return get_slashing_resistance();
			case "crushing": return get_crushing_resistance();
			default: return 0;
		}

	}

	private int get_piercing_resistance(){
		int amount = 0;
		for(EquipmentSlot slot : EquipmentSlot.values()){
			if(equipment.get(slot) != null){
				if(entityManager.gc(equipment.get(slot), Armor.class) != null) {
					amount += entityManager.gc(equipment.get(slot), Armor.class).piercing;
				}
			}
		}
		return amount;
	}

	private int get_slashing_resistance(){
		int amount = 0;
		for(EquipmentSlot slot : EquipmentSlot.values()){
			if(equipment.get(slot) != null){
				if(entityManager.gc(equipment.get(slot), Armor.class) != null) {
					amount += entityManager.gc(equipment.get(slot), Armor.class).slashing;
				}
			}
		}
		return amount;
	}

	private int get_crushing_resistance(){
		int amount = 0;
		for(EquipmentSlot slot : EquipmentSlot.values()){
			if(equipment.get(slot) != null){
				if(entityManager.gc(equipment.get(slot), Armor.class) != null) {
					amount += entityManager.gc(equipment.get(slot), Armor.class).crushing;
				}
			}
		}
		return amount;
	}

	private ArrayList<Damage> get_base_damage(){
		ArrayList<Damage> damages = new ArrayList<>();
		damages.add(new Damage("crushing", new Dice(1, 3)));
		return damages;
	}

	private ArrayList<Damage> get_left_damage(){
		return entityManager.gc(equipment.get(EquipmentSlot.LEFT_HAND), OffensiveComponent.class).damages;
	}

	private ArrayList<Damage> get_right_damage(){
		return entityManager.gc(equipment.get(EquipmentSlot.RIGHT_HAND), OffensiveComponent.class).damages;
	}

	private ArrayList<Damage> get_dual_wield_damages(){
		ArrayList<Damage> temp = new ArrayList<>(entityManager.gc(equipment.get(EquipmentSlot.RIGHT_HAND), OffensiveComponent.class).damages);
		temp.addAll(entityManager.gc(equipment.get(EquipmentSlot.LEFT_HAND), OffensiveComponent.class).damages);

		return temp;
	}

	public ArrayList<Damage> get_melee_damages(){

		if(equipment.get(EquipmentSlot.LEFT_HAND) != null && equipment.get(EquipmentSlot.RIGHT_HAND) != null){

			if(entityManager.gc(equipment.get(EquipmentSlot.LEFT_HAND), OffensiveComponent.class) != null
					&& entityManager.gc(equipment.get(EquipmentSlot.RIGHT_HAND), OffensiveComponent.class) != null){
				return get_dual_wield_damages();
			}
			else if(entityManager.gc(equipment.get(EquipmentSlot.RIGHT_HAND), OffensiveComponent.class) != null
					&& entityManager.gc(equipment.get(EquipmentSlot.LEFT_HAND), OffensiveComponent.class) == null){
				return get_right_damage();
			}
			else if(entityManager.gc(equipment.get(EquipmentSlot.LEFT_HAND), OffensiveComponent.class) != null
					&& entityManager.gc(equipment.get(EquipmentSlot.RIGHT_HAND), OffensiveComponent.class) == null){
				return get_left_damage();
			}
			else
				return get_base_damage();

		}
		else if(equipment.get(EquipmentSlot.LEFT_HAND) != null && equipment.get(EquipmentSlot.RIGHT_HAND) == null){
			if(entityManager.gc(equipment.get(EquipmentSlot.LEFT_HAND), OffensiveComponent.class) != null){
				return get_left_damage();
			}
			else
				return get_base_damage();
		}
		else if(equipment.get(EquipmentSlot.RIGHT_HAND) != null && equipment.get(EquipmentSlot.LEFT_HAND) == null){
			if(entityManager.gc(equipment.get(EquipmentSlot.RIGHT_HAND), OffensiveComponent.class) != null){
				return get_right_damage();
			}
			else
				return get_base_damage();
		}
		else
			return get_base_damage();
	}

	public void equip_item(Integer owner, Integer item, EquipmentSlot slot){

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

	public void equip_item_from_inventory(Integer owner, Integer item, EquipmentSlot slot){

		if(entityManager.gc(item, Equippable.class).slots.contains(slot) && equipment.get(slot) == null){
			entityManager.gc(owner, Inventory.class).inventory.remove(item);
			equipment.put(slot, item);
		}
		else if(entityManager.gc(item, Equippable.class).slots.contains(slot) && equipment.get(slot) != null){
			entityManager.gc(owner, Inventory.class).add_item(equipment.remove(slot));
		}
		else{
			System.out.println("That doesn't go there.");
		}
	}

	public void unequip_item(EquipmentSlot slot, Integer owner){
		Integer item = equipment.get(slot);
		equipment.remove(slot, equipment.get(slot));
		entityManager.gc(owner, Inventory.class).add_item(item);
	}
}
