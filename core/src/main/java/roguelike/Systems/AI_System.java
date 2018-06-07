package roguelike.Systems;

import roguelike.Actions.Move;
import roguelike.Components.AI;
import roguelike.Components.Action_Component;
import roguelike.utilities.Point;
import roguelike.utilities.Roll;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class AI_System implements Base_System {

	public ArrayList<Integer> actors;

	public AI_System(ArrayList<Integer> actors){
		this.actors = actors;
	}

	@Override
	public void process() {

		for (Integer current_actor : actors) {
			String action;
			if (entityManager.gc(current_actor, AI.class) != null) {
				action = entityManager.gc(current_actor, AI.class).get_decision();

				switch (action) {
					case "wait":
						entityManager.gc(current_actor, Action_Component.class).setAction(new Move(current_actor, Point.WAIT));
						break;
					case "move east":
						entityManager.gc(current_actor, Action_Component.class).setAction(new Move(current_actor, Point.EAST));
						break;
					case "wander":
						entityManager.gc(current_actor, Action_Component.class).setAction(
								new Move(current_actor, Point.direction.get(Roll.rand(0, Point.direction.size() - 1))));
						break;
				}
			}
		}
	}
}
