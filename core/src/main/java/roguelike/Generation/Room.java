package roguelike.Generation;

import lombok.Getter;
import lombok.Setter;
import roguelike.utilities.Roll;
import squidpony.squidmath.Coord;

@Getter @Setter
public class Room {
    private Coord topLeft;
    private Coord bottomRight;

    public Room(int x, int y, int width, int height){

        this.topLeft = Coord.get(x, y);
        this.bottomRight = Coord.get(x + width - 1, y + height - 1);
    }

    public boolean intersects(Room second){

        return (this.getTopLeft().x <= second.bottomRight.x + Roll.rand(3, 5)) && (this.getBottomRight().x + Roll.rand(3, 5) >= second.getTopLeft().x) && (this.getTopLeft().y <= second.getBottomRight().y + Roll.rand(3, 5)) && (this.getBottomRight().y + Roll.rand(3, 5) >= second.getTopLeft().y);
    }
}