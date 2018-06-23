package roguelike.Components;

import java.util.ArrayList;

public class Inventory implements Component{

	public ArrayList<Integer> inventory;

	public Inventory(){
		inventory = new ArrayList<>();
	}

	public void add_item(Integer item){
		inventory.add(item);
	}

	public void remove_item(Integer item){
		inventory.remove(item);
	}
}
