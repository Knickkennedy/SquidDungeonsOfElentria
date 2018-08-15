package roguelike.Actions.Animations;

import roguelike.Components.Sprite;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidmath.Coord;

public class MoveAnimation extends SingleTileAnimation implements Animation{

    private Sprite sprite;
    private Coord startLocation;
    private Coord endLocation;
    private int priority;

    public MoveAnimation(Sprite sprite, Coord startLocation, Coord endLocation){
        this.sprite = sprite;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.priority = 1;
    }

    @Override
    public int getPriority(){
        return priority;
    }

    @Override
    public void createAnimation(SparseLayers display){

        if(sprite.getGlyph() != null) {
            display.slide(sprite.getGlyph(), startLocation.x, startLocation.y, endLocation.x, endLocation.y, 0.03f, null);
        }
    }
}
