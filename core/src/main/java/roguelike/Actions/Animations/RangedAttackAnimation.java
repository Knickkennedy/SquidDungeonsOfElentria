package roguelike.Actions.Animations;

import squidpony.squidgrid.gui.gdx.PanelEffect;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;

import java.util.ArrayList;

public class RangedAttackAnimation extends MultiTileAnimation implements Animation{

    private GreasedRegion greasedRegion;
    private ArrayList<Coord> coords;
    private char shown;
    private SColor color;
    private int priority;

    public RangedAttackAnimation(GreasedRegion greasedRegion, ArrayList<Coord> coords, char shown, SColor color){
        this.greasedRegion = greasedRegion;
        this.coords = coords;
        this.shown = shown;
        this.color = color;
        this.priority = 3;
    }

    @Override
    public void createAnimation(SparseLayers display) {
    	if(coords.size() > 1)
            display.addAction(new PanelEffect.SteadyProjectileEffect(display, 0.15f, greasedRegion, coords.get(0), coords.get(coords.size() - 1), shown, color));
    	}

    @Override
    public int getPriority() {
        return priority;
    }
}
