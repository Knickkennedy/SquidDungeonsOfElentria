package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.utilities.LimitedStatistic;

public class Statistics implements Component{

	public LimitedStatistic health;

	public LimitedStatistic strength;
	public LimitedStatistic intelligence;
	public LimitedStatistic willpower;
	public LimitedStatistic constitution;
	public LimitedStatistic dexterity;
	public LimitedStatistic charisma;

	public Statistics(JSONObject object){

		for(Object o : object.keySet()){
			switch (o.toString()){
				case "health"       : health = new LimitedStatistic((int)(long)object.get(o.toString()), (int)(long)object.get(o.toString())); break;
				case "strength"     : strength = new LimitedStatistic((int)(long)object.get(o.toString())); break;
				case "intelligence" : intelligence = new LimitedStatistic((int)(long)object.get(o.toString())); break;
				case "willpower"    : willpower = new LimitedStatistic((int)(long)object.get(o.toString())); break;
				case "constitution" : constitution = new LimitedStatistic((int)(long)object.get(o.toString())); break;
				case "dexterity"    : dexterity = new LimitedStatistic((int)(long)object.get(o.toString())); break;
				case "charisma"     : charisma = new LimitedStatistic((int)(long)object.get(o.toString())); break;
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

		System.out.println(this);
	}

	public LimitedStatistic get_stat(String type){
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
