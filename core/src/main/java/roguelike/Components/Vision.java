package roguelike.Components;

import lombok.Getter;
import roguelike.Generation.Map;
import roguelike.utilities.Point;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.Radius;
import squidpony.squidgrid.mapping.DungeonUtility;

@Getter
public class Vision extends Component{

	private Point location;
	private Map map;
	private double range;
	private double[][] res, fov;

	public Vision(Point point, Map map, double range){
		this.location = point;
		this.map = map;
		this.range = range;
		res = DungeonUtility.generateSimpleResistances(map.pathfinding);
		fov = new double[res.length][res[0].length];
		FOV.reuseFOV(this.res, this.fov, this.location.x, this.location.y, this.range, Radius.CIRCLE);
	}

	public void setLocation(Point direction){
//		this.location.x += direction.x;
//		this.location.y += direction.y;
		FOV.reuseFOV(this.res, this.fov, this.location.x, this.location.y, this.range, Radius.CIRCLE);
	}
}
