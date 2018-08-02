package roguelike.Components;

import roguelike.Enums.AIMODE;
import roguelike.utilities.LineOfSight;

public class AI implements Component{

	public AIMODE mode;
	public LineOfSight los;
	public Integer current_target;
	public boolean has_seen;

	public AI(){
		this.los = new LineOfSight();
		mode = AIMODE.AGGRESSIVE;
		this.has_seen = false;
	}
}
