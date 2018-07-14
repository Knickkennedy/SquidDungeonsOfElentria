package roguelike.utilities;

import com.badlogic.gdx.graphics.Color;

public class Colors {
    private static Color green = new Color(0.0f, 0.73f, 0.0f, 1.0f);
    private static Color blue = new Color(0f, 0.5f, 1.0f, 1.0f);
    private static Color darkGreen = new Color(0.0f, .35f, 0.0f, 1.0f);
    private static Color brown = new Color(.4f, .2f, 0.0f, 1.0f);
    private static Color gray = new Color(.6f, .6f, .6f, 1.0f);
    private static Color lightGray = new Color(.75f, .75f, .75f, 1.0f);
    private static Color darkGray = new Color(.45f, .45f, .45f, 1.0f);
    private static Color white = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private static Color magenta = new Color(.73f, 0.0f, .73f, 1.0f);
    private static Color brightMagenta = new Color(1.0f, 0.0f, 1.0f, 1.0f);
    private static Color darkMagenta = new Color(.5f, 0.0f, .5f, 1.0f);

    public static Color getColor(String name){
        switch(name){
			case "green": return Colors.green;
			case "blue": return Colors.blue;
			case "dark green": return Colors.darkGreen;
			case "brown": return Colors.brown;
			case "gray": return Colors.gray;
			case "light gray": return Colors.lightGray;
			case "dark gray": return Colors.darkGray;
            case "white": return Colors.white;
			case "magenta": return Colors.magenta;
			case "bright magenta": return Colors.brightMagenta;
			case "dark magenta": return Colors.darkMagenta;
            default: return Colors.white;
        }
    }
}
