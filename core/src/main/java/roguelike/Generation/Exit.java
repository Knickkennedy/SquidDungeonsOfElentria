package roguelike.Generation;

import roguelike.utilities.Point;

import java.util.Objects;

import static roguelike.Generation.World.first_dungeon_location;
import static roguelike.engine.Game.*;

public class Exit{

	public Dungeon leads_to_dungeon;
	public int floor;
	public Point player_coordinates;
	public String put_at;

	public Exit(Dungeon leads_to, int floor_to, String put_at){
		this.leads_to_dungeon = leads_to;
		floor = floor_to;
		this.put_at = put_at;
	}

	public Map go_through(){

		if(floor < leads_to_dungeon.size && floor > 0 && !leads_to_dungeon.level_exists(floor)){
			leads_to_dungeon.build_level(gridWidth, gridHeight - statistics_height);
			player_coordinates = leads_to_dungeon.getLevel(floor).stairs_up;
		}
		else if(floor == 0 && leads_to_dungeon.name.equals("Main Dungeon")){
			player_coordinates = first_dungeon_location;
		}
		else if(put_at.equals("stairs - up")){
			player_coordinates = leads_to_dungeon.getLevel(floor).stairs_up;
		}
		else if(put_at.equals("stairs - down")){
			player_coordinates = leads_to_dungeon.getLevel(floor).stairs_down;
		}

		return leads_to_dungeon.getLevel(floor);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Exit exit = (Exit) o;
		return floor == exit.floor &&
				Objects.equals(leads_to_dungeon, exit.leads_to_dungeon) &&
				Objects.equals(player_coordinates, exit.player_coordinates) &&
				Objects.equals(put_at, exit.put_at);
	}

	@Override
	public int hashCode() {

		return Objects.hash(leads_to_dungeon, floor, player_coordinates, put_at);
	}
}
