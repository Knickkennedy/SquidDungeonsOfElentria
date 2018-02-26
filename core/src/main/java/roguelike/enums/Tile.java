package roguelike.enums;

import lombok.Getter;
import lombok.Setter;
import roguelike.utilities.Colors;
import roguelike.utilities.Sprite;

public enum Tile {
    Wall(new Sprite('#', Colors.darkGray), "wall", "a crumbling wall", true),
    Closed_Door(new Sprite('+', Colors.brown), "closed door", "a closed door", true),
    Open_Door(new Sprite('/', Colors.white), "opened door", "an opened door", false),
    Stairs_Up(new Sprite('<', Colors.gray), "stairway leading up", "a stairway leading up", false),
    Stairs_Down(new Sprite('>', Colors.gray), "stairway leading down", "a stairway leading down", false),
    Grass(new Sprite('"', Colors.green), "grass", "fresh grass", false),
    Water(new Sprite('=', Colors.blue), "water", "fresh water", false),
    Mountain(new Sprite('^', Colors.gray), "mountain", "a jagged mountain", true),
    Forest(new Sprite('&', Colors.darkGreen), "forest", "a multitude of trees", false),
    Road(new Sprite('.', Colors.brown), "road", "a road", false),
    Cave(new Sprite('*', Colors.darkGray), "cave", "a scary cave", false),
    Floor(new Sprite('.', Colors.green), "dirt", "dirty floor", false);

    @Getter
    @Setter
    private boolean solid;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private Sprite sprite;

    Tile(Sprite sprite, String name, String description, boolean solid){
        this.sprite = sprite;
        this.name = name;
        this.description = description;
        this.solid = solid;
    }
}
