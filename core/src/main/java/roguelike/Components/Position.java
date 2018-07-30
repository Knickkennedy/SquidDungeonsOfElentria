package roguelike.Components;

import roguelike.Generation.Map;
import squidpony.squidgrid.gui.gdx.SquidPanel;
import squidpony.squidmath.Coord;

public class Position implements Component{

	public Coord location;
	public Map map;

	public Position(){}
	public Position(Map map){
		this.map = map;
	}

	public void update_location(Coord direction){
		location = location.add(direction);
	}
}
