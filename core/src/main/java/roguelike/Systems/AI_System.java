package roguelike.Systems;

import roguelike.Actions.Move;
import roguelike.Components.*;
import roguelike.Enums.AI_MODE;
import roguelike.utilities.Point;
import roguelike.utilities.Roll;
import squidpony.squidai.DijkstraMap;
import squidpony.squidmath.Coord;

import java.util.ArrayList;

import static roguelike.Generation.World.entityManager;

public class AI_System implements Base_System {

	public ArrayList<Integer> actors;
	public DijkstraMap path;

	public AI_System(ArrayList<Integer> actors){
		this.actors = actors;
	}

	@Override
	public void process() {

		for (Integer current_actor : actors) {
			if (entityManager.gc(current_actor, AI.class) != null) {
				if(entityManager.gc(current_actor, Action_Component.class).getAction() == null) {

					if(path == null){
						path = new DijkstraMap(entityManager.gc(current_actor, Position.class).map.pathfinding, DijkstraMap.Measurement.EUCLIDEAN);
					}

					switch (entityManager.gc(current_actor, AI.class).mode) {
						case PASSIVE:
							entityManager.gc(current_actor, Action_Component.class).setAction(new Move(current_actor, Point.WAIT));
							break;
						case NEUTRAL:
							entityManager.gc(current_actor, Action_Component.class).setAction(
									new Move(current_actor, Point.direction.get(Roll.rand(0, Point.direction.size() - 1))));
							break;
						case AGGRESSIVE:
							perform_hunt_attempt(current_actor, path);
							break;
					}
				}
			}
		}
	}

	public void perform_hunt_attempt(Integer current_actor, DijkstraMap path){

		ArrayList<Coord> monster_locations = new ArrayList<>();

		for(Integer actor : actors){
			if(!entityManager.gc(actor, Details.class).isPlayer && !current_actor.equals(actor)){
				monster_locations.add(entityManager.gc(actor, Position.class).location);
			}
		}

		for(Integer actor : actors){

			if(entityManager.gc(current_actor, AI.class).has_seen && entityManager.gc(current_actor, AI.class).current_target.equals(actor)){
				path.setGoal(entityManager.gc(actor, Position.class).location);
				ArrayList<Coord> coords =
						path.findPath(1, monster_locations, null,
								entityManager.gc(current_actor, Position.class).location, entityManager.gc(actor, Position.class).location);
				entityManager.gc(current_actor, Action_Component.class).setAction(
								new Move(current_actor, coords.get(0).subtract(entityManager.gc(current_actor, Position.class).location)));
				break;
			}
			else if(entityManager.gc(actor, Details.class).isPlayer && can_see(current_actor, actor)){

				if(!entityManager.gc(current_actor, AI.class).has_seen) {
					entityManager.gc(current_actor, AI.class).has_seen = true;
					entityManager.gc(current_actor, AI.class).current_target = actor;
				}

				path.setGoal(entityManager.gc(actor, Position.class).location);
				ArrayList<Coord> coords =
								path.findPath(1, monster_locations, null,
								entityManager.gc(current_actor, Position.class).location, entityManager.gc(actor, Position.class).location);
				entityManager.gc(current_actor, Action_Component.class).setAction(
						new Move(current_actor, coords.get(0).subtract(entityManager.gc(current_actor, Position.class).location)));
				break;
			}
			entityManager.gc(current_actor, Action_Component.class).setAction(
						new Move(current_actor, Point.direction.get(Roll.rand(0, Point.direction.size() - 1))));
		}
	}

	public boolean can_see(Integer current_actor, Integer target){
		Coord center = entityManager.gc(current_actor, Position.class).location;
		Coord target_location = entityManager.gc(target, Position.class).location;

		int range = (center.x - target_location.x)*(center.x - target_location.x) + (center.y - target_location.y)*(center.y - target_location.y);
		double vision_radius = entityManager.gc(current_actor, Vision.class).getRange()*entityManager.gc(current_actor, Vision.class).getRange();

		boolean is_reachable = entityManager.gc(current_actor, AI.class).
				los.isReachable(entityManager.gc(current_actor, Position.class).map, center.x, center.y, target_location.x, target_location.y);

		if(is_reachable){
			return vision_radius > range;
		}

		return false;
	}
}
