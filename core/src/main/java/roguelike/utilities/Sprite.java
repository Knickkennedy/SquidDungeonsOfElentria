package roguelike.utilities;

import org.json.simple.JSONObject;

import java.awt.*;

public class Sprite {
    public char character;
    public Color backgroundColor;
    public Color foregroundColor;

    public Sprite(JSONObject object){
        this.character = getChar((String)object.get("glyph"));
        this.foregroundColor = Colors.getColor((String)object.get("color"));
        this.backgroundColor = Color.black;
    }

    public Sprite(char character, Color foregroundColor){
        this.character = character;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = Color.black;
    }

    private char getChar(String string){
        return string.charAt(0);
    }
}
