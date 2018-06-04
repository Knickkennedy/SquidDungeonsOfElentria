package roguelike.Generation;

import roguelike.utilities.Point;

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

	public void build_dungeon(){
		for(int i = 1; i < size; i++){
			if(i < size - 1) {
				Map map = new Map(gridWidth, gridHeight - statistics_height);
				map.buildStandardLevel();
				Exit exit = new Exit(this, map.stairs_up, i - 1, "stairs - down");
				Exit exit2 = new Exit(this, map.stairs_down, i + 1, "stairs - up");
				levels.add(map);
				levels.get(i).exits.add(exit);
				levels.get(i).exits.add(exit2);
			}
			else{
				Map map = new Map(gridWidth, gridHeight - statistics_height);
				map.build_final_level();
				Exit exit = new Exit(this, map.stairs_up, i - 1, "stairs - down");
				levels.add(map);
				levels.get(i).exits.add(exit);
			}
		}
	}

	public void print_exits(){
		for(int i = 0; i < levels.size(); i++){
			for(Exit exit : levels.get(i).exits){
				System.out.println(exit.floor);
			}
		}
	}

	public Map getLevel(int i){
			return levels.get(i);
	}
}
