package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Enums.Equipment_Slot;

import java.util.ArrayList;
import java.util.Set;

public class Equippable extends Component{
	public ArrayList<Equipment_Slot> slots;

	public Equippable(JSONObject object){
		slots = new ArrayList<>();

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "head": slots.add(Equipment_Slot.HEAD); break;
				case "chest": slots.add(Equipment_Slot.CHEST); break;
			}
		}
	}
}
