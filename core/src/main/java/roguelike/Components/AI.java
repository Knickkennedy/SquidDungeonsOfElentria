package roguelike.Components;

import roguelike.Enums.AI_MODE;
import roguelike.utilities.Line_Of_Sight;
import squidpony.squidai.DijkstraMap;

public class AI implements Component{

	public AI_MODE mode;
	public Line_Of_Sight los;

	public AI(){
		this.los = new Line_Of_Sight();
		mode = AI_MODE.AGGRESSIVE;
	}

	public AI_MODE getMode(){
		return mode;
	}
}
