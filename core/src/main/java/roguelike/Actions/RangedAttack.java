package roguelike.Actions;

import roguelike.Actions.Animations.Animation;
import roguelike.Actions.Animations.RangedAttackAnimation;
import roguelike.Components.ActionComponent;
import roguelike.Components.Energy;
import roguelike.Components.Equipment;
import roguelike.Components.Position;
import roguelike.Effects.Damage;
import roguelike.Systems.DeathSystem;
import roguelike.engine.MessageLog;
import squidpony.squidgrid.gui.gdx.PanelEffect;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static roguelike.Generation.World.entityManager;
import static roguelike.engine.Game.*;

public class RangedAttack extends Action{

    private Integer entity; //Entity making attack
    private Queue<Coord> line; //Our initial line of fire
    private ArrayList<Coord> displayLine;
    private SparseLayers display; //Needed for animations
    private List<Animation>animations;

    public RangedAttack(Integer entity, Queue<Coord> lineIn, SparseLayers display, List<Animation>animations){
        this.entity = entity;
        this.line = lineIn;
        this.displayLine = new ArrayList<>();
        this.display = display;
        this.animations = animations;
        this.cost = 1000;
    }

    @Override
    public boolean canPerform(){
        return entityManager.gc(entity, Energy.class).energy >= cost;
    }

    @Override
    public boolean perform() {

        Position position = entityManager.gc(entity, Position.class);
        for(Coord coord : line){

            if(position.location.equals(coord))
                continue;

            if(position.map.isSolid(coord.x, coord.y))
                break;

            Integer target = position.map.entityAt(coord);
            if(target != null){

                int damageAmount = 0;

                for(Damage damage : entityManager.gc(entity, Equipment.class).getRangedDamage()){
                    int defenseValue = entityManager.gc(target, Equipment.class).get_resistance_from_type(damage.type);

                    damageAmount += damage.roll() - defenseValue;

                    if(damageAmount < 0)
                        damageAmount = 0;
                }

                MessageLog.getInstance().add_formatted_message("shoot", entity, target, damageAmount);

                new DeathSystem(entity, target, "health", -damageAmount).process();

                break;
            }

            displayLine.add(coord.translate(0, message_buffer));
        }

        GreasedRegion greasedRegion = new GreasedRegion(gridWidth, gridHeight);
        greasedRegion.allOn();
        animations.add(new RangedAttackAnimation(greasedRegion, displayLine, '/', SColor.CHESTNUT_LEATHER_BROWN));
/*
        display.addAction(new PanelEffect.ProjectileEffect(display, 1f, greasedRegion, displayLine.get(0), displayLine.get(displayLine.size() - 1), '/', SColor.CHESTNUT_LEATHER_BROWN));
*/

        entityManager.gc(entity, Energy.class).energy -= cost;
        entityManager.gc(entity, ActionComponent.class).setAction(null);
        return true;
    }

    @Override
    public boolean isAlternativeAction() {
        return false;
    }
}
