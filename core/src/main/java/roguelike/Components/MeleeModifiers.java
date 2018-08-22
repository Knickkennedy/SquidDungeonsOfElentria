package roguelike.Components;

import org.json.simple.JSONObject;

public class MeleeModifiers implements Component{
	public int toHitBonus;
	public int damageBonus;

	public MeleeModifiers(JSONObject object){
		for(Object o : object.keySet()){
			switch (o.toString()){
				case "to hit": toHitBonus = (int)(long)object.get(o.toString()); break;
				case "damage": damageBonus = (int)(long)object.get(o.toString()); break;
			}
		}
	}

	public MeleeModifiers(){
		this.toHitBonus = 0;
		this.damageBonus = 0;
	}

	@Override
	public String toString(){
		return String.format("To Hit: %d, Damage Bonus: %d", toHitBonus, damageBonus);
	}
}
