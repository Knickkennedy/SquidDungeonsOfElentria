package roguelike.utilities;

import java.awt.*;

public class Colors {
    public static Color green = new Color(0, 185, 0);
    public static Color blue = new Color(0, 0, 160);
    public static Color darkGreen = new Color(0, 90, 0);
    public static Color brown = new Color(102, 51, 0);
    public static Color gray = new Color(130, 130, 130);
    public static Color lightGray = new Color(190, 190, 190);
    public static Color darkGray = new Color(60, 60, 60);
    public static Color white = new Color(255, 255, 255);
    public static Color magenta = new Color(190, 0, 190);
    public static Color brightMagenta = new Color(255, 0, 255);
    public static Color darkMagenta = new Color(128, 0, 128);

    public static Color getColor(String name){
        switch(name){
            case "white": return Colors.white;
            default: return Colors.white;
        }
    }
}
