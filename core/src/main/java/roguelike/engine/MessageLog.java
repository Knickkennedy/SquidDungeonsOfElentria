package roguelike.engine;

import roguelike.Components.Details;

import java.util.LinkedList;

import static roguelike.Generation.World.entityManager;

public class MessageLog {

	public static MessageLog message_log = null;
	public LinkedList<String> messages;
	public int ticks;

	private MessageLog(){
		messages = new LinkedList<>();
	}

	public void check_ticks(){
		if(ticks > 10) {
			ticks = 0;
			messages.clear();
		}
	}

	public void addEffectMessage(String message, Integer ...entities){
		if(entities.length == 1){
			if(entityManager.gc(entities[0], Details.class).isPlayer){
				messages.add(String.format("You %s.", message));
			}
			else{
				messages.add(String.format("The %s %s.", entityManager.gc(entities[0], Details.class).name, structure_verb(message)));
			}
		}
	}

	public void add_formatted_message(String message, Integer ...entities){

		if(entities.length == 1){
			if(entityManager.gc(entities[0], Details.class).isPlayer){
				messages.add(String.format("You %s.", message));
			}
			else{
				messages.add(String.format("The %s %s.", entityManager.gc(entities[0], Details.class).name, structure_verb(message)));
			}
		}
		else if(entities.length == 3){
			if(entityManager.gc(entities[0], Details.class).isPlayer){
				messages.add(String.format("You %s the %s for %d damage.",
						message, entityManager.gc(entities[1], Details.class).name, entities[2]));
			}
			else if(entityManager.gc(entities[1], Details.class).isPlayer){
				messages.add(String.format("The %s %s you for %d damage.",
						entityManager.gc(entities[0], Details.class).name, structure_verb(message), entities[2]));
			}
			else
				messages.add(String.format("The %s %s the %s for %d damage.", entityManager.gc(entities[0], Details.class).name,
						structure_verb(message), entityManager.gc(entities[1], Details.class).name, entities[2]));
		}
	}

	public String structure_verb(String message){
		String[] words = message.split(" ");
		if(isAnAdverb(words[0])){
			if (shouldEndWithES(words[1])) {
				words[1] = words[1] + "es";
			} else {
				words[1] = words[1] + "s";
			}
		}
		else {
			if (shouldEndWithES(words[0])) {
				words[0] = words[0] + "es";
			} else {
				words[0] = words[0] + "s";
			}
		}
		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			builder.append(" ");
			builder.append(word);
		}

		return builder.toString().trim();
	}

	private boolean isAnAdverb(String word){
		return word.toLowerCase().matches(".*(ly)");
	}

	private boolean shouldEndWithES(String word){
		return word.toLowerCase().matches(".*(s|sh|x|ch|z)");
	}

	public static MessageLog getInstance(){
		if(message_log == null){
			message_log = new MessageLog();
		}

		return message_log;
	}
}
