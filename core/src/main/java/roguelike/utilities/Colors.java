package roguelike.utilities;

import squidpony.squidgrid.gui.gdx.SColor;

public class Colors {
//    private static Color green = new Color(0.0f, 0.73f, 0.0f, 1.0f);
//    private static Color blue = new Color(0f, 0.5f, 1.0f, 1.0f);
//    private static Color darkGreen = new Color(0.0f, .35f, 0.0f, 1.0f);
//    private static Color brown = new Color(.4f, .2f, 0.0f, 1.0f);
//    private static Color gray = new Color(.6f, .6f, .6f, 1.0f);
//    private static Color lightGray = new Color(.75f, .75f, .75f, 1.0f);
//    private static Color darkGray = new Color(.45f, .45f, .45f, 1.0f);
//    private static Color white = new Color(1.0f, 1.0f, 1.0f, 1.0f);
//    private static Color magenta = new Color(.73f, 0.0f, .73f, 1.0f);
//    private static Color brightMagenta = new Color(1.0f, 0.0f, 1.0f, 1.0f);
//    private static Color darkMagenta = new Color(.5f, 0.0f, .5f, 1.0f);

    // you can look up a doc preview for any SColor to see the color in different contexts
    private static final SColor green = SColor.CW_GREEN;
    private static final SColor blue = SColor.CW_BLUE;
    private static final SColor darkGreen = SColor.CW_DRAB_GREEN;
    private static final SColor brown = SColor.CW_DARK_BROWN;
    private static final SColor gray = SColor.CW_GRAY;
    private static final SColor lightGray = SColor.CW_GRAY_WHITE;
    private static final SColor darkGray = SColor.CW_DARK_GRAY;
    private static final SColor white = SColor.WHITE;
    private static final SColor magenta = SColor.CW_FLUSH_MAGENTA;
    private static final SColor brightMagenta = SColor.CW_BRIGHT_MAGENTA;
    private static final SColor darkMagenta = SColor.CW_DARK_MAGENTA;

    public static SColor getColor(String name){
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
