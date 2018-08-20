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
	private double[][] res, fov;

	public Vision(Coord point, Map map, double range){
		this.location = point;
		this.map = map;
		this.range = range;
		res = DungeonUtility.generateSimpleResistances(map.pathfinding);
		fov = new double[res.length][res[0].length];
		FOV.reuseFOV(this.res, this.fov, this.location.x, this.location.y, this.range, Radius.CIRCLE);
	}

	public void setLocation(Coord mapPosition){
		location = mapPosition;
		FOV.reuseFOV(res, fov, location.x, location.y, range, Radius.CIRCLE);
	}

	public void refreshVision(){
		res = DungeonUtility.generateSimpleResistances(map.pathfinding);
		fov = new double[res.length][res[0].length];
		FOV.reuseFOV(this.res, this.fov, this.location.x, this.location.y, this.range, Radius.CIRCLE);
	}
}
