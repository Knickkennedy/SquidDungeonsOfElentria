package roguelike.Actions.Animations;

import squidpony.squidgrid.gui.gdx.SparseLayers;

public interface Animation {
    void createAnimation(SparseLayers display);
    int getPriority();
}
