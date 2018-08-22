package roguelike.Components;

import org.json.simple.JSONObject;

public class Creature implements Component{
    public Armor armor;
    public OffensiveComponent attack;
    public MeleeModifiers meleeModifiers;

    public Creature(JSONObject object){
        for(Object o : object.keySet()){
            switch (o.toString()){
                case "armor": armor = new Armor((JSONObject)object.get(o.toString())); break;
                case "attack": attack = new OffensiveComponent((JSONObject)object.get(o.toString())); break;
                case "melee modifiers": meleeModifiers = new MeleeModifiers((JSONObject)object.get(o.toString())); break;
            }
        }
    }

    public int get_resistance_from_type(String type){

        switch (type){
            case "piercing": return armor.piercing;
            case "slashing": return armor.slashing;
            case "crushing": return armor.crushing;
            default: return 0;
        }
    }

    @Override
    public String toString(){
        return armor.toString() + attack.toString() + meleeModifiers.toString();
    }
}
