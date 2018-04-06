package roguelike.utilities;

import com.badlogic.gdx.graphics.Color;

public class Colors {
    public static Color green = new Color(0.0f, 0.73f, 0.0f, 1.0f);
    public static Color blue = new Color(0f, 0f, 0.6f, 1.0f);
    public static Color darkGreen = new Color(0.0f, .35f, 0.0f, 1.0f);
    public static Color brown = new Color(.4f, .2f, 0.0f, 1.0f);
    public static Color gray = new Color(.6f, .6f, .6f, 1.0f);
    public static Color lightGray = new Color(.75f, .75f, .75f, 1.0f);
    public static Color darkGray = new Color(.45f, .45f, .45f, 1.0f);
    public static Color white = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static Color magenta = new Color(.73f, 0.0f, .73f, 1.0f);
    public static Color brightMagenta = new Color(1.0f, 0.0f, 1.0f, 1.0f);
    public static Color darkMagenta = new Color(.5f, 0.0f, .5f, 1.0f);

    public static Color getColor(String name){
        switch(name){
            case "white": return Colors.white;
            default: return Colors.white;
        }
    }
}
