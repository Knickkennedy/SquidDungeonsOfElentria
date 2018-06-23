package roguelike.Components;

import roguelike.Enums.AI_MODE;

public class AI implements Component{

	public AI_MODE mode;

	public String get_decision(){
		return "wander";
	}
}
