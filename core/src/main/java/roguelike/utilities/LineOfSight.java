package roguelike.utilities;

import roguelike.Generation.Map;
import squidpony.squidgrid.LOS;
import squidpony.squidgrid.Radius;

public class LineOfSight extends LOS {

	public double[][] resMap;

	public LineOfSight(){
		super();
	}

	public boolean isReachable(Map map, int startX, int startY, int targetX, int targetY){

		if(resMap == null) {
			resMap = new double[map.width()][map.height()];

			for(int x = 0; x < map.width(); x++){
				for(int y = 0; y < map.height(); y++){
					resMap[x][y] = map.isSolid(x, y) ? 1.0 : 0.0;
				}
			}
		}

		return isReachable(resMap, startX, startY, targetX, targetY, Radius.CIRCLE);
	}
}
