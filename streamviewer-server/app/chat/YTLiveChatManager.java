package chat;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import model.ChatMessage;
import play.libs.ws.WSClient;

public class YTLiveChatManager {
	private static final int DELAY = 1000;
	private static final YTLiveChatManager ytlcm = new YTLiveChatManager();

	private WSClient ws;
	private ConcurrentHashMap<String, Set<ActorRef>> chatIdVsActors = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Timer> chatIdVsTimer = new ConcurrentHashMap<>();

	private YTLiveChatManager() {
	}

	public static YTLiveChatManager getInstance() {
		return ytlcm;
	}

	public void setWSClient(WSClient ws) {
		if (this.ws == null) {
			this.ws = ws;
		}
	}

	public void addActor(final ChatMessage cm, ActorRef actor) {
		Set<ActorRef> set = chatIdVsActors.get(cm.getLiveChatId());
		if (set == null) {
			set = new HashSet<ActorRef>();
			chatIdVsActors.put(cm.getLiveChatId(), set);
			Timer t = new Timer();
			t.schedule(new YTLiveChatMessageFetchTask(t, ws, cm.getLiveChatId(), cm.getVideoId(), cm.getTitle(),
					cm.getDescription(), null, 0), DELAY);

			chatIdVsTimer.put(cm.getLiveChatId(), t);
		}
		set.add(actor);
	}

	public void removeActor(final String videoId, final ActorRef actor) {
		Set<ActorRef> set = chatIdVsActors.get(videoId);
		if (set != null) {
			set.remove(actor);
		}
	}

	public void removeActor(final ActorRef actor) {
		Set<String> keySet = chatIdVsActors.keySet();
		for (String liveChatId : keySet) {
			removeActor(liveChatId, actor);
		}

		cleanUp();
		// cleanUp
	}

	private void cleanUp() {
		chatIdVsActors.keySet().stream().filter(key -> {
			Set<ActorRef> actors = chatIdVsActors.get(key);
			return actors == null || actors.isEmpty();
		}).iterator().forEachRemaining(s -> {
			chatIdVsTimer.get(s).cancel();
			chatIdVsTimer.remove(s);
			chatIdVsActors.remove(s);
		});
	}
}
