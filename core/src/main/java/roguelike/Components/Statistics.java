package roguelike.Components;

import org.json.simple.JSONObject;
import roguelike.utilities.Limited_Statistic;

public class Statistics extends Component{

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
				case "health": health = new Limited_Statistic((int)(long)object.get(o.toString()), (int)(long)object.get(o.toString())); break;

				case "strength": strength = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "intelligence": intelligence = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "willpower": willpower = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "constitution": constitution = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "dexterity": dexterity = new Limited_Statistic((int)(long)object.get(o.toString())); break;
				case "charisma": charisma = new Limited_Statistic((int)(long)object.get(o.toString())); break;
			}
		}
	}
}
