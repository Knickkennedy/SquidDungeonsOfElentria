package roguelike.Actions;

import roguelike.Actions.Animations.Animation;
import roguelike.Actions.Animations.MeleeAttackAnimation;
import roguelike.Components.*;
import roguelike.Effects.Damage;
import roguelike.Systems.DeathSystem;
import roguelike.engine.MessageLog;
import roguelike.utilities.Dice;
import roguelike.utilities.Roll;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;

import java.util.List;

import static roguelike.Generation.World.entityManager;

public class MeleeAttack extends Action{

	private Integer attacker;
	private Integer target;
	private SparseLayers display;
	private List<Animation>animations;

	public MeleeAttack(Integer attacker, Integer target, SparseLayers display, List<Animation> animations){
		this.attacker = attacker;
		this.target = target;
		this.cost = 1000;
		this.display = display;
		this.animations = animations;
	}

	@Override
	public boolean canPerform(){
		return entityManager.gc(attacker, Energy.class).energy >= cost;
	}

	@Override
	public boolean perform() {

		Statistics statistics = entityManager.gc(target, Statistics.class);
		if(statistics != null) {
			Sprite sprite = entityManager.gc(attacker, Sprite.class);
			Coord attackerLocation = entityManager.gc(attacker, Position.class).location;
			Coord defenderLocation = entityManager.gc(target, Position.class).location;
			animations.add(new MeleeAttackAnimation(sprite, Direction.getDirection(defenderLocation.x - attackerLocation.x, defenderLocation.y - attackerLocation.y)));

			int damage = 0;
			String type;

			Body attackerBody;
			Body defenderBody;
			if(entityManager.gc(attacker, Details.class).isHumanoid){
				attackerBody = entityManager.gc(attacker, Equipment.class);
			}
			else{
				attackerBody = entityManager.gc(attacker, Creature.class);
			}

			if(entityManager.gc(target, Details.class).isHumanoid){
				defenderBody = entityManager.gc(target, Equipment.class);
			}
			else{
				defenderBody = entityManager.gc(target, Creature.class);
			}

			MeleeModifiers attackerMeleeModifiers = attackerBody.getMeleeModifiers();

			for(Damage dam : attackerBody.getMeleeDamages()){
				String[] types = dam.type.split("/");
				int numberOfTypes = types.length;

				type = types[Roll.rand(0, numberOfTypes - 1)];
				int defensiveAmount = defenderBody.getResistanceFromType(type);
				damage += getActualDamage(dam, attackerMeleeModifiers.damageBonus, defensiveAmount);

				if(damage <= 0){
					damage = 0;
				}
				else{
					if(attackerBody.getOnHitEffects() != null) {
						for (OnHitEffect onHitEffect : attackerBody.getOnHitEffects()) {
							double roll = Roll.d100();
							double comparedTo = 100.0 - onHitEffect.procChance;

							System.out.println(roll);

							if(roll >= comparedTo)
								onHitEffect.apply(target);
						}
					}
				}
			}

			MessageLog.getInstance().add_formatted_message("attack", attacker, target, damage);

			new DeathSystem(attacker, target, "health", - damage).process();

			entityManager.gc(attacker, Energy.class).energy -= cost;
			entityManager.gc(attacker, ActionComponent.class).setAction(null);

			return true;
		}
		else{
			entityManager.gc(attacker, ActionComponent.class).setAction(null);

			return true;
		}
	}

	private int getActualDamage(Damage damage, int damageBonus, int defensiveStat){
		return damage.roll() + damageBonus - defensiveStat;
	}

	@Override
	public boolean isAlternativeAction() {
		return false;
	}
}
