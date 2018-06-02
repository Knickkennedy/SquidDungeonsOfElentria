package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Enums.Equipment_Slot;

import java.util.ArrayList;
import java.util.Set;

public class Equippable extends Component{
	ArrayList<Equipment_Slot> slots;

	public Equippable(JSONObject object){
		slots = new ArrayList<>();

		for(Object o : object.keySet()){
			switch (o.toString()){
				
			}
		}
	}
}
