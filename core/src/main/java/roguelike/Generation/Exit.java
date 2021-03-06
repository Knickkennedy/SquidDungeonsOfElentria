package roguelike.Generation;

import squidpony.squidmath.Coord;

import static roguelike.Generation.World.first_dungeon_location;

public class Exit{

	public Dungeon leads_to_dungeon;
	public final int floor;
	public Coord player_coordinates;
	public String put_at;
	public final Coord exit_location;

	public Exit(Dungeon leads_to, final Coord exit_location, final int floor_to, String put_at){
		this.leads_to_dungeon = leads_to;
		this.exit_location = exit_location;
		floor = floor_to;
		this.put_at = put_at;
	}

	public void set_player_location(){
		if(floor == 0 && leads_to_dungeon.name.equals("Main Dungeon")){
			player_coordinates = first_dungeon_location;
		}
		else if(put_at.equals("stairs - up")){
			player_coordinates = leads_to_dungeon.getLevel(floor).stairs_up;
		}
		else if(put_at.equals("stairs - down")){
			player_coordinates = leads_to_dungeon.getLevel(floor).stairs_down;
		}
	}

	public Map go_through(){
		return leads_to_dungeon.getLevel(floor);
	}
}
