package chat;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import model.ChatMessage;

public final class ChatStatsManager {

	private static final int INTERVAL = 500; // Every 500 milli seconds

	private static final ChatStatsManager sm = new ChatStatsManager(); // Eager initialization

	// channel vs List of messages
	private ConcurrentHashMap<String, Set<ChatMessage>> channelMessages = new ConcurrentHashMap<>();
	// Actor vs videoId
	private ConcurrentHashMap<ActorRef, String> channelVsVideoId = new ConcurrentHashMap<>();

	private Timer timer = new Timer();

	private ChatStatsManager() {
		ChatStatsTimerTask cstt = new ChatStatsTimerTask(channelMessages, channelVsVideoId);

		long delay = 1000L;
		timer.scheduleAtFixedRate(cstt, delay, INTERVAL);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			timer.cancel();
		}));
	}

	public static ChatStatsManager getInstance() {
		return sm;
	}

	public void addMessage(final ChatMessage cm) {
		Set<ChatMessage> set = channelMessages.get(cm.getVideoId());
		if (set == null) {
			set = new HashSet<ChatMessage>();
			channelMessages.put(cm.getVideoId(), set);
		}
		set.add(cm);
	}

	public void addChannel(final ActorRef actor, final String videoId) {
		channelVsVideoId.put(actor, videoId);
	}

	public void removeChannel(final ActorRef actor) {
		channelVsVideoId.remove(actor);
	}

	// public void addActiveVideoId(final String videoId) {
	// activeVideos.add(videoId);
	// }
	//
	// public void removeActiveVideoId(final String videoId) {
	// // Removes the first occurrence
	// activeVideos.remove(videoId);
	// }
}
