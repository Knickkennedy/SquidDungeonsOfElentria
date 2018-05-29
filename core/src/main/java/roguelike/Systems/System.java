package roguelike.Systems;

import roguelike.Generation.Map;

public interface System {
	public int process(int current_actor);
}
