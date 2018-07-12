package roguelike.Enums;

public enum Race implements Hostility{

	GOBLIN,
	HUMAN,
	PLAYER;

	public static Race get_race(String type){

		switch (type){
			case "goblin"   :   return GOBLIN;
			case "human"    :   return HUMAN;
			case "player"   :   return PLAYER;
			default: return null;
		}
	}
}
