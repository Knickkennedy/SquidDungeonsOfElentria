package roguelike.Generation;

import java.util.ArrayList;

import static roguelike.engine.Game.*;

public class Dungeon {

	public String name;
	public ArrayList<Map> levels;
	public int size;

	public Dungeon(String name, int size){
		this.name = name;
		levels = new ArrayList<>();
		this.size = size;
	}

	public void add_level(int index, Map level){
		levels.add(index, level);
	}



	public void build_basic_dungeon(){
		for(int i = 1; i < size; i++){
			if(i < size - 1) {
				Map map = new Map(gridWidth, gridHeight - statistics_height);
				levels.add(map);

			}
			else{
				Map map = new Map(gridWidth, gridHeight - statistics_height);
				levels.add(map);
			}
		}
	}

	public Map getLevel(int i){

		if(levels.get(i).isBuilt)
			return levels.get(i);
		else if(i < size - 1){
			levels.get(i).buildStandardLevel();
			Exit exit = new Exit(this, levels.get(i).stairs_up, i - 1, "stairs - down");
			Exit exit2 = new Exit(this, levels.get(i).stairs_down, i + 1, "stairs - up");
			levels.get(i).exits.add(exit);
			levels.get(i).exits.add(exit2);

			return levels.get(i);
		}
		else{
			levels.get(i).build_final_level();
			Exit exit = new Exit(this, levels.get(i).stairs_up, i - 1, "stairs - down");
			levels.get(i).exits.add(exit);

			return levels.get(i);
		}
	}
}
