package roguelike.utilities;

public class Limited_Statistic {
	public int minimum;
	public int maximum;
	public int current_value;

	public Limited_Statistic(int minimum, int maximum, int current_value){
		this.minimum = minimum;
		this.maximum = maximum;
		this.current_value = current_value;
	}

	public Limited_Statistic(int current_value, int maximum){
		this.minimum = 1;
		this.maximum = maximum;
		this.current_value = current_value;
	}

	public Limited_Statistic(int current_value){
		this.minimum = 1;
		this.maximum = 999;
		this.current_value = current_value;
	}

	public void setValue(int value){
		if(value < minimum)
			this.current_value = minimum;
		else if(value > maximum)
			this.current_value = maximum;
		else
			this.current_value = value;
	}

	public void changeValue(int value){
		if(current_value + value < minimum)
			current_value = minimum;
		else if(current_value + value > maximum)
			current_value = maximum;
		else
			current_value += value;
	}

	@Override
	public String toString(){
		return String.valueOf(current_value);
	}
}
