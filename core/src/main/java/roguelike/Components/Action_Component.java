package roguelike.Components;

import lombok.Getter;
import lombok.Setter;
import roguelike.Actions.Action;

@Getter @Setter
public class Action_Component extends Component{
	private Action action;

	public Action_Component(){ }
}
