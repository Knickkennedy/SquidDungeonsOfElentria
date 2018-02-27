package roguelike.screens;

import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Start_Screen {

    private StretchViewport startViewPort;

    public Start_Screen(int grid_width, int grid_height, int cell_width, int cell_height){
        this.startViewPort = new StretchViewport(grid_width * cell_width, grid_height * cell_height);
        startViewPort.setScreenBounds(0, 0, grid_width * cell_width, grid_height * cell_height);
    }
}
