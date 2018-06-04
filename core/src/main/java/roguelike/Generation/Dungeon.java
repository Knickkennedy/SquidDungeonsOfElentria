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

	public void build_level(int width, int height){
		Map map = new Map(width, height);
		if(levels.size() < size) {
			map.buildStandardLevel();
			final Point point = map.stairs_up;
			final Point point2 = map.stairs_down;
			map.exits.put(point, new Exit(this, levels.size() - 1, "stairs - down"));
			map.exits.put(point2, new Exit(this, levels.size() + 1, "stairs - up"));
			levels.add(map);
		}
		else if(levels.size() == size){
			map.build_final_level();
			map.exits.put(map.stairs_up, new Exit(this, levels.size() - 1, "stairs - down"));
			levels.add(map);
		}
	}

	public boolean level_exists(int level){
		if(level > size)
			return false;

		if(level < 0)
			return false;

		return level < levels.size();
	}

	public Map getLevel(int i){
			return levels.get(i);
	}
}
