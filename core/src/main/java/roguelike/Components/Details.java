package roguelike.Components;

import org.json.simple.JSONObject;

public class Details extends Component{
	public String name;
	public String description;

	public Details(String name, JSONObject object){
		this.name = name;
		description = (String)object.get("description");
	}
}
