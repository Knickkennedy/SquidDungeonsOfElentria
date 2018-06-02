package roguelike.Components;

import roguelike.Enums.Equipment_Slot;

import java.util.HashMap;

public class Equipment extends Component{
	public HashMap<Equipment_Slot, Integer> equipment;

	public Equipment(){
		equipment = new HashMap<>();

		for(Equipment_Slot slot : Equipment_Slot.values()){
			equipment.put(slot, null);
		}
	}
}
