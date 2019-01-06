package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.Effects.Damage;

import java.util.ArrayList;

public class Creature extends Body{
    public Armor armor;
    public OffensiveComponent meleeAttack;
    public MeleeModifiers meleeModifiers;
    public OffensiveComponent rangedAttack;
    public RangedModifiers rangedModifiers;
    public ArrayList<OnHitEffect> onHitEffects;

    public Creature(JSONObject object){
    	this.onHitEffects = new ArrayList<>();
        for(Object o : object.keySet()){
            switch (o.toString()){
                case "armor": armor = new Armor((JSONObject)object.get(o.toString())); break;
                case "melee attack": meleeAttack = new OffensiveComponent((JSONObject)object.get(o.toString())); break;
                case "melee modifiers": meleeModifiers = new MeleeModifiers((JSONObject)object.get(o.toString())); break;
	            case "ranged attack": rangedAttack = new OffensiveComponent((JSONObject)object.get(o.toString())); break;
	            case "ranged modifiers": rangedModifiers = new RangedModifiers((JSONObject)object.get(o.toString())); break;
	            case "on hit effect": onHitEffects.add(new OnHitEffect((JSONObject)object.get(o.toString()))); break;
            }
        }
    }

    @Override
    public int getResistanceFromType(String type){

        switch (type){
            case "piercing": return armor.piercing;
            case "slashing": return armor.slashing;
            case "crushing": return armor.crushing;
            default: return 0;
        }
    }

    @Override
    public MeleeModifiers getMeleeModifiers(){
    	return meleeModifiers;
    }

    @Override
    public ArrayList<Damage> getMeleeDamages(){
    	return meleeAttack.damages;
    }

    @Override
    public RangedModifiers getRangedModifiers(){ return rangedModifiers; }

    @Override
    public ArrayList<Damage> getRangedDamages(){ return rangedAttack.damages; }

    @Override
    public ArrayList<OnHitEffect> getOnHitEffects(){ return onHitEffects; }

    @Override
    public String toString(){
        return armor.toString() + meleeAttack.toString() + meleeModifiers.toString();
    }
}
