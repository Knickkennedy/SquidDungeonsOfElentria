package roguelike.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import roguelike.engine.Game;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new Game(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Dungeons of Elentria";
        configuration.width = 112 * 10;
        configuration.height = 32 * 20;
        for (int size : new int[] { 128, 64, 32, 16 }) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
}