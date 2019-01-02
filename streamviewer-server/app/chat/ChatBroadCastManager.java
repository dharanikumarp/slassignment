package chat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import akka.actor.ActorRef;
import model.ChatMessage;
import play.libs.Json;

/**
 * Manages the Actors (Rooms) that are currently active and broadcast message.
 * 
 * @author dharani kumar palani
 */
public class ChatBroadCastManager {

	private static final ChatBroadCastManager cbm = new ChatBroadCastManager(); // Eager initialization
	private ConcurrentHashMap<String, Set<ActorRef>> channelVsActors = new ConcurrentHashMap<>();

	public static ChatBroadCastManager getInstance() {
		return cbm;
	}

	public void addActor(final String videoId, ActorRef actor) {
		Set<ActorRef> set = channelVsActors.get(videoId);
		if (set == null) {
			set = new HashSet<ActorRef>();
			channelVsActors.put(videoId, set);
		}
		set.add(actor);
	}

	public void removeActor(final String videoId, final ActorRef actor) {
		Set<ActorRef> set = channelVsActors.get(videoId);
		if (set != null) {
			set.remove(actor);
		}
	}

	public void removeActor(final ActorRef actor) {
		Set<String> keySet = channelVsActors.keySet();
		for (String videoId : keySet) {
			removeActor(videoId, actor);
		}

		// cleanUp
		cleanUp();
	}

	
	private void cleanUp() {
		channelVsActors.keySet().stream().filter(key -> {
			Set<ActorRef> actors = channelVsActors.get(key);
			return actors == null || actors.isEmpty();
		}).iterator().forEachRemaining(s -> {
			channelVsActors.remove(s);
		});
	}

	public void broadCastMessage(final ChatMessage cm) {
		Set<ActorRef> set = channelVsActors.get(cm.getVideoId());
		if (set != null && !set.isEmpty()) {
			for (ActorRef actor : set) {
				actor.tell(Json.toJson(cm), null);
			}
		}
	}
}
