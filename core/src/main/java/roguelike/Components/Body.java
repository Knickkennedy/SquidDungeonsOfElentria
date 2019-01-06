package roguelike.Components;

import roguelike.Effects.Damage;

import java.util.ArrayList;

public abstract class Body implements Component{
	public abstract int getResistanceFromType(String type);
	public abstract MeleeModifiers getMeleeModifiers();
	public abstract ArrayList<Damage> getMeleeDamages();
	public abstract RangedModifiers getRangedModifiers();
	public abstract ArrayList<Damage> getRangedDamages();
	public abstract ArrayList<OnHitEffect> getOnHitEffects();
}
