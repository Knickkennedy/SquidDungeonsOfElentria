package roguelike.Components;

import lombok.Getter;
import roguelike.Generation.Map;
import roguelike.utilities.Point;

public class Position extends Component{

	public Point location;
	public Map map;

	public Position(Map map){
		this.map = map;
	}

	public void update_location(Point direction){
		this.location.x += direction.x;
		this.location.y += direction.y;
	}
}
