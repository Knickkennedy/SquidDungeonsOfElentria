package roguelike.Enums;

public enum Equipment_Slot {
	CHEST,
	LEFT_HAND,
	RIGHT_HAND;

	@Override
	public String toString() {
		String name = this.name().toLowerCase();
		name = name.replace("_", " ");
		return name;
	}
}
