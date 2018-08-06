package roguelike.Enums;

import roguelike.utilities.Word;

public enum EquipmentSlot {
	HEAD,
	CHEST,
	LEFT_HAND,
	RIGHT_HAND,
	RANGED_WEAPON,
	AMMUNITION;

	public static EquipmentSlot find_slot(String name){

		switch (name){
			case "head": return HEAD;
			case "chest": return CHEST;
			case "left hand": return LEFT_HAND;
			case "right hand": return RIGHT_HAND;
			case "ranged weapon": return RANGED_WEAPON;
			case "ammunition": return AMMUNITION;
			default: return null;
		}

	}

	@Override
	public String toString() {
		String name = this.name().toLowerCase();
		name = name.replace("_", " ");










		return Word.capitalize_all(name);
	}
}
