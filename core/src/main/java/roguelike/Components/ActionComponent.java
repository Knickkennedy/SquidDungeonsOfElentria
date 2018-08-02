package roguelike.Components;

import lombok.Setter;
import roguelike.Actions.Action;

@Setter
public class ActionComponent implements Component{
	private Action action;

	public ActionComponent(){ }

	public Action getAction(){
		return action;
	}

	public boolean hasAction(){
		return action != null;
	}
}
