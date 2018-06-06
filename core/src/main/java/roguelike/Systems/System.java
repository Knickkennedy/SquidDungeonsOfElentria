package roguelike.Systems;

import roguelike.Generation.Map;

public interface System {
	int process(int current_actor);
}
