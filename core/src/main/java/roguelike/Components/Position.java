package roguelike.Components;

import lombok.Getter;
import roguelike.Generation.Map;
import roguelike.utilities.Point;

@Getter
public class Position extends Component{

	private Point location;
	private Map map;

	public Position(Point point, Map map){
		this.location = point;
		this.map = map;
	}

	public void setLocation(Point direction){
		this.location.x += direction.x;
		this.location.y += direction.y;
	}
}
