package roguelike.Components;

import lombok.Setter;
import roguelike.Actions.Action;

@Setter
public class Action_Component extends Component{
	private Action action;

	public Action_Component(){ }

	public Action getAction(){
		return action;
	}

	public boolean hasAction(){
		return action != null;
	}
}
