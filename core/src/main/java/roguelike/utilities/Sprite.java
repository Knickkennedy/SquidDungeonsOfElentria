package roguelike.utilities;

import com.badlogic.gdx.graphics.Color;
import org.json.simple.JSONObject;

public class Sprite {
    public char character;
    public Color backgroundColor;
    public Color foregroundColor;

    public Sprite(JSONObject object){
        this.character = getChar((String)object.get("glyph"));
        this.foregroundColor = Colors.getColor((String)object.get("color"));
        this.backgroundColor = Color.BLACK;
    }

    public Sprite(char character, Color foregroundColor){
        this.character = character;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = Color.BLACK;
    }

    private char getChar(String string){
        return string.charAt(0);
    }
}
