package roguelike.Components;

import org.json.simple.JSONObject;

public class RangedModifiers implements Component{
	public int toHitBonus;
	public int damageBonus;

	public RangedModifiers(JSONObject object){
		for(Object o : object.keySet()){
			switch (o.toString()){
				case "to hit": toHitBonus = (int)(long)object.get(o.toString()); break;
				case "damage":	damageBonus = (int)(long)object.get(o.toString()); break;
			}
		}
	}

	public RangedModifiers(){
		this.toHitBonus = 0;
		this.damageBonus = 0;
	}
}
