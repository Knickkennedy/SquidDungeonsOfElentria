package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Enums.EquipmentSlot;

import java.util.ArrayList;

public class Equippable implements Component{
	public ArrayList<EquipmentSlot> slots;

	public Equippable(JSONObject object){
		slots = new ArrayList<>();

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "head": slots.add(EquipmentSlot.HEAD); break;
				case "chest": slots.add(EquipmentSlot.CHEST); break;
				case "left hand": slots.add(EquipmentSlot.LEFT_HAND); break;
				case "right hand": slots.add(EquipmentSlot.RIGHT_HAND); break;
			}
		}
	}
}
