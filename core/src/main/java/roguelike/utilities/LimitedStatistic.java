package roguelike.utilities;

public class LimitedStatistic {
	public int minimum;
	public int maximum;
	public int current_value;

	public LimitedStatistic(int minimum, int maximum, int current_value){
		this.minimum = minimum;
		this.maximum = maximum;
		this.current_value = current_value;
	}

	public LimitedStatistic(int current_value, int maximum){
		this.minimum = 0;
		this.maximum = maximum;
		this.current_value = current_value;
	}

	public LimitedStatistic(int current_value){
		this.minimum = 0;
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

	public boolean isMinimum(){
		return current_value == minimum;
	}

	@Override
	public String toString(){
		return String.valueOf(current_value);
	}
}
