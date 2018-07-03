package roguelike.Enums;

import roguelike.utilities.Word;

public enum Equipment_Slot {
	HEAD,
	CHEST,
	LEFT_HAND,
	RIGHT_HAND;

	public static Equipment_Slot find_slot(String name){

		switch (name){
			case "head": return HEAD;
			case "chest": return CHEST;
			case "left hand": return LEFT_HAND;
			case "right hand": return RIGHT_HAND;
			default: return null;
		}

	}

	@Override
	public String toString() {
		String name = this.name().toLowerCase();
		name = name.replace("_", " ");










		return Word.capitalize_all(name);
	}
	
	public static final Equipment_Slot[] ALL = values();
}
