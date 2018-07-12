package roguelike.Components;

import roguelike.Enums.AI_MODE;
import roguelike.utilities.Line_Of_Sight;

public class AI implements Component{

	public AI_MODE mode;
	public Line_Of_Sight los;
	public Integer current_target;
	public boolean has_seen;

	public AI(){
		this.los = new Line_Of_Sight();
		mode = AI_MODE.AGGRESSIVE;
		this.has_seen = false;
	}
}
