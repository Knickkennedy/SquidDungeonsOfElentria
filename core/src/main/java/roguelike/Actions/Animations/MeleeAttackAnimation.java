package roguelike.Actions.Animations;

import roguelike.Components.Sprite;
import squidpony.squidgrid.Direction;
import squidpony.squidgrid.gui.gdx.SparseLayers;

public class MeleeAttackAnimation extends SingleTileAnimation implements Animation{

    private Sprite sprite;
    private Direction direction;
    private int priority;

    public MeleeAttackAnimation(Sprite sprite, Direction direction){
        this.sprite = sprite;
        this.direction = direction;
        this.priority = 2;
    }

    @Override
    public int getPriority(){
        return priority;
    }

    @Override
    public void createAnimation(SparseLayers display) {
        display.bump(sprite.getGlyph(), direction, 0.05f);
    }
}
