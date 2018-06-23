package roguelike.utilities;

import org.json.simple.JSONObject;

public class Dice {

	int number_of;
	int size;

	public Dice(JSONObject object){
		this.number_of = (int)(long)object.get("number of");
		this.size = (int)(long)object.get("size");
	}

	public Dice(int number_of, int size){
		this.number_of = number_of;
		this.size = size;
	}

	public int roll(){

		int x = 0;

		for(int i = 0; i < number_of; i++){
			x += Roll.rand(1, size);
		}

		return x;

	}


	@Override
	public String toString(){
		return String.format("%dd%d", number_of, size);
	}
}
