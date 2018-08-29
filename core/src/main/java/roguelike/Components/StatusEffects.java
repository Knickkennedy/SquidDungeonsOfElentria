package roguelike.Components;

import roguelike.Effects.Effect;

import java.util.ArrayList;

public class StatusEffects implements Component{
	public ArrayList<Effect> statusEffects;

	public StatusEffects(){
		this.statusEffects = new ArrayList<>();
	}

	public void add(Effect effect){
		statusEffects.add(effect);
	}

	public Effect get(int index){
		return statusEffects.get(index);
	}
}
