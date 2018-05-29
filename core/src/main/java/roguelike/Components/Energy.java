package roguelike.Components;

public class Energy extends Component{

	public int speed;
	public int energy;

	public Energy(int speed){
		this.speed = speed;
		this.energy = 0;
	}
}
