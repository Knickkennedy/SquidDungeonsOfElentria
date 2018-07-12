package roguelike.Components;

import lombok.Getter;
import roguelike.Generation.Map;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.Radius;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidmath.Coord;

@Getter
public class Vision implements Component{

	private Coord location;
	private Map map;
	private double range;
	private double[][] fov;

	public Vision(Coord point, Map map, double range){
		this.location = point;
		this.map = map;
		this.range = range;
		if(map.res == null) {
			map.res = DungeonUtility.generateResistances(map.pathfinding);
		}
		fov = new double[map.res.length][map.res[0].length];
		FOV.reuseFOV(map.res, this.fov, this.location.x, this.location.y, this.range, Radius.CIRCLE);
	}

	public void setLocation(Coord mapPosition) {
		if (mapPosition.isWithin(map.width(), map.height())) {
			location = mapPosition;
			FOV.reuseFOV(map.res, fov, location.x, location.y, range, Radius.CIRCLE);
		}
	}
}
