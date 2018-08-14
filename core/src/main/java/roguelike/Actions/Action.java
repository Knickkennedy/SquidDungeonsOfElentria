package roguelike.Actions;

public abstract class Action {
	public abstract boolean canPerform();
	public abstract boolean perform();
	public abstract boolean isAlternativeAction();
	public int cost;
}
