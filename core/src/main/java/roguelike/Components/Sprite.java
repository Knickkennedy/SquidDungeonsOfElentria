package roguelike.Components;

import com.badlogic.gdx.graphics.Color;
import lombok.Getter;
import org.json.simple.JSONObject;
import roguelike.utilities.Colors;
import squidpony.squidgrid.gui.gdx.SparseLayers;
import squidpony.squidgrid.gui.gdx.TextCellFactory;

@Getter
public class Sprite implements Component {
    public char character;
    public Color backgroundColor;
    public Color foregroundColor;

    private TextCellFactory.Glyph glyph;

    public Sprite(JSONObject object){
        this.character = getChar((String)object.get("glyph"));
        this.foregroundColor = Colors.getColor((String)object.get("color"));
        this.backgroundColor = Color.BLACK;
    }

    private char getChar(String string){
        return string.charAt(0);
    }

	public TextCellFactory.Glyph makeGlyph(SparseLayers display, int gridX, int gridY) {
		if (glyph == null)
			glyph = display.glyph(character, foregroundColor, gridX, gridY);
		else {
			if (! display.glyphs.contains(glyph))
				display.glyphs.add(glyph);
			glyph.setPosition(display.worldX(gridX), display.worldY(gridY));
		}
		return glyph;
	}
}
