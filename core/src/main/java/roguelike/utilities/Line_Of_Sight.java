package roguelike.utilities;

import roguelike.Generation.Map;
import squidpony.squidgrid.LOS;
import squidpony.squidgrid.Radius;

public class Line_Of_Sight extends LOS {

	public Line_Of_Sight(){
		super();
	}

	public boolean isReachable(Map map, int startX, int startY, int targetX, int targetY){
		return isReachable(map.res, startX, startY, targetX, targetY, Radius.CIRCLE);
	}
}
