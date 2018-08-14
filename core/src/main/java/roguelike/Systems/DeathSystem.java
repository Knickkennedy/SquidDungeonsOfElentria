package roguelike.Systems;

import roguelike.Components.Statistics;
import roguelike.Generation.Factory;

import static roguelike.Generation.World.entityManager;

public class DeathSystem implements BaseSystem {

	public Integer attacker, defender, amount;
	public String stat_affected;

	public DeathSystem(Integer attacker, Integer defender, String stat_affected, Integer amount){
		this.attacker = attacker;
		this.defender = defender;
		this.stat_affected = stat_affected;
		this.amount = amount;
	}

	@Override
	public void process() {
		entityManager.gc(defender, Statistics.class).get_stat(stat_affected).changeValue(amount);

		if(entityManager.gc(defender, Statistics.class).get_stat(stat_affected).isMinimum() && !Factory.getInstance().death_queue.contains(defender)){
			Factory.getInstance().death_queue.add(defender);
		}
	}

	@Override
	public void process(Integer actor) {

	}
}
