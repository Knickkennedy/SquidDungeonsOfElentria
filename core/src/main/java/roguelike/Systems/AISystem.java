package roguelike.Systems;

import roguelike.Actions.Animations.Animation;
import roguelike.Actions.Move;
import roguelike.Components.*;
import roguelike.utilities.Point;
import roguelike.utilities.Roll;
import squidpony.squidai.DijkstraMap;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;

import java.util.ArrayList;
import java.util.List;

import static roguelike.Generation.World.entityManager;

public class AISystem implements BaseSystem {

	private ArrayList<Integer> actors;
	private Integer currentActor;
	private SparseLayers display;
	private List<Animation>animations;
	public DijkstraMap path;

	public AISystem(ArrayList<Integer> actors, SparseLayers display, List<Animation>animations){
		this.actors = actors;
		this.display = display;
		this.animations = animations;
	}

	@Override
	public void process() { }

	@Override
	public void process(Integer actor) {

		this.currentActor = actor;

		if (entityManager.gc(actor, AI.class) != null) {
			if (entityManager.gc(actor, ActionComponent.class).getAction() == null) {

				if (path == null) {
					path = new DijkstraMap(entityManager.gc(actor, Position.class).map.pathfinding, DijkstraMap.Measurement.EUCLIDEAN);
				}

				switch (entityManager.gc(actor, AI.class).mode) {
					case DEBUG:
						entityManager.gc(actor, ActionComponent.class).setAction(new Move(actor, Point.WAIT, display, animations));
						break;
					case PASSIVE:
						entityManager.gc(actor, ActionComponent.class).setAction(new Move(actor, Point.WAIT, display, animations));
						break;
					case NEUTRAL:
						entityManager.gc(actor, ActionComponent.class).setAction(
								new Move(actor, Point.direction.get(Roll.rand(0, Point.direction.size() - 1)), display, animations));
						break;
					case AGGRESSIVE:
						perform_hunt_attempt();
						break;
				}
			}
		}
	}

	public void perform_hunt_attempt(){

		ArrayList<Coord> monster_locations = new ArrayList<>();

		for(Integer otherActor : actors){

			if(!entityManager.gc(currentActor, Details.class).is_hostile_towards(otherActor) && !currentActor.equals(otherActor)){
				monster_locations.add(entityManager.gc(otherActor, Position.class).location);
			}
		}

		for(Integer actor : actors){

			if(entityManager.gc(currentActor, AI.class).has_seen && entityManager.gc(currentActor, AI.class).current_target.equals(actor)){
				path.setGoal(entityManager.gc(actor, Position.class).location);
				ArrayList<Coord> coords =
						path.findPath(1, monster_locations, null,
								entityManager.gc(currentActor, Position.class).location, entityManager.gc(actor, Position.class).location);
				entityManager.gc(currentActor, ActionComponent.class).setAction(
								new Move(currentActor, coords.get(0).subtract(entityManager.gc(currentActor, Position.class).location), display, animations));
				break;
			}
			else if(entityManager.gc(currentActor, Details.class).is_hostile_towards(actor) && can_see(currentActor, actor)){

				if(!entityManager.gc(currentActor, AI.class).has_seen) {
					entityManager.gc(currentActor, AI.class).has_seen = true;
					entityManager.gc(currentActor, AI.class).current_target = actor;
				}

				path.setGoal(entityManager.gc(actor, Position.class).location);
				ArrayList<Coord> coords =
								path.findPath(1, monster_locations, null,
								entityManager.gc(currentActor, Position.class).location, entityManager.gc(actor, Position.class).location);
				entityManager.gc(currentActor, ActionComponent.class).setAction(
						new Move(currentActor, coords.get(0).subtract(entityManager.gc(currentActor, Position.class).location), display, animations));
				break;
			}
			entityManager.gc(currentActor, ActionComponent.class).setAction(
						new Move(currentActor, Point.direction.get(Roll.rand(0, Point.direction.size() - 1)), display, animations));
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
