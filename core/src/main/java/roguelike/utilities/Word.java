package roguelike.utilities;

public class Word {

	public static String capitalize_all(String name){
		String[] words = name.split(" ");

		name = "";

		for(int i = 0; i < words.length; i++){
			words[i] = words[i].substring(0, 1).toUpperCase().concat(words[i].substring(1));
		}

		for(int i = 0; i < words.length - 1; i++){
			name = name.concat(words[i]).concat(" ");
		}

		name = name.concat(words[words.length - 1]);

		return name;
	}
}
