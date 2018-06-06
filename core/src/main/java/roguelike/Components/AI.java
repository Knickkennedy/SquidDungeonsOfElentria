package roguelike.Components;

import roguelike.Actions.Move;
import roguelike.Enums.AI_MODE;
import roguelike.utilities.Point;

public class AI extends Component{

	public AI_MODE mode;

	public String get_decision(){
		return "wander";
	}
}
