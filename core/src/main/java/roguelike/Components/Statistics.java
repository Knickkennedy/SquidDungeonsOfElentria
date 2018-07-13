package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.utilities.Limited_Statistic;

public class Statistics implements Component{

	public Limited_Statistic health;

	public Limited_Statistic strength;
	public Limited_Statistic intelligence;
	public Limited_Statistic willpower;
	public Limited_Statistic constitution;
	public Limited_Statistic dexterity;
	public Limited_Statistic charisma;

	public Statistics(JSONObject object){

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "health"       : health = new Limited_Statistic((int)(long)object.get(o.toString()), (int)(long)object.get(o.toString())); break;
				case "strength"     : strength = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "intelligence" : intelligence = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "willpower"    : willpower = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "constitution" : constitution = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "dexterity"    : dexterity = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "charisma"     : charisma = new Limited_Statistic((int)(long)object.get(o.toString())); break;
			}
		}
	}

	public void update_base_stats(JSONObject object){

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "health"       : health.changeValue((int)(long)object.get(o.toString())); break;
				case "strength"     : strength.changeValue((int)(long)object.get(o.toString())); break;
				case "intelligence" : intelligence.changeValue((int)(long)object.get(o.toString())); break;
				case "willpower"    : willpower.changeValue((int)(long)object.get(o.toString())); break;
				case "constitution" : constitution.changeValue((int)(long)object.get(o.toString())); break;
				case "dexterity"    : dexterity.changeValue((int)(long)object.get(o.toString())); break;
				case "charisma"     : charisma.changeValue((int)(long)object.get(o.toString())); break;
			}
		}

		//System.out.println(this);
	}

	public Limited_Statistic get_stat(String type){
		switch (type){
			case "health"       :   return health;
			case "strength"     :   return strength;
			case "intelligence" :   return intelligence;
			case "willpower"    :   return willpower;
			case "constitution" :   return constitution;
			case "dexterity"    :   return dexterity;
			case "charisma"     :   return charisma;
			default: return null;
		}
	}

	@Override
	public String toString(){
		return String.format("%s, %s, %s, %s, %s, %s", strength, intelligence, willpower, constitution, dexterity, charisma);
	}
}
