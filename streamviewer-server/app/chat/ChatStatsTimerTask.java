package chat;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import model.ChatMessage;
import play.libs.Json;
import stats.ChatStats;
import stats.RollingTimeChatStats;
import stats.UserChatStats;
import utils.ChatMessageUtil;

import org.bson.Document;

public class ChatStatsTimerTask extends TimerTask {

	private final ConcurrentHashMap<String, Set<ChatMessage>> channelMessages;
	private final ConcurrentHashMap<ActorRef, String> channelVsVideoId;

	public ChatStatsTimerTask(final ConcurrentHashMap<String, Set<ChatMessage>> channelMessages,
			final ConcurrentHashMap<ActorRef, String> channels) {
		this.channelMessages = channelMessages;
		this.channelVsVideoId = channels;
	}

	@Override
	public void run() {

		if (channelVsVideoId.isEmpty()) {
			// System.out.println("No active connections.");
			return;
		}

		Set<String> keyset = channelMessages.keySet();
		List<RollingTimeChatStats> rtcsList = new ArrayList<>();

		for (String string : keyset) {
			long now = new Date().getTime();

			Set<ChatMessage> chatMessages = channelMessages.get(string);

			long m1 = countMessagesPerInterval(chatMessages, 1 * 1000);
			long m5 = countMessagesPerInterval(chatMessages, 5 * 1000);
			long m10 = countMessagesPerInterval(chatMessages, 10 * 1000);
			long m30 = countMessagesPerInterval(chatMessages, 30 * 1000);

			List<ChatMessage> messagesToRemove = new ArrayList<>();
			for (ChatMessage chatMessage : chatMessages) {
				if (now - chatMessage.getTime().getTime() > 60 * 1000) {
					messagesToRemove.add(chatMessage);
				}
			}
			chatMessages.removeAll(messagesToRemove);
			long min = chatMessages.size();

			RollingTimeChatStats rtcs = new RollingTimeChatStats(string, m1, m5, m10, m30, min);
			rtcsList.add(rtcs);
		}

		keyset = channelMessages.keySet();
		List<ChatStats> statList = new ArrayList<>();

		for (String string : keyset) {
			Set<ChatMessage> chatMessages = channelMessages.get(string);
			int numMsgs = chatMessages != null && !chatMessages.isEmpty() ? chatMessages.size() : 0;
			statList.add(new ChatStats(string, numMsgs));
		}

		//keyset = channelVsVideoId.keySet();
		// Map<String, List<UserChatStats>> userMessagesInVideo = new HashMap<>();
		
		Set<ActorRef> actorSet = channelVsVideoId.keySet();
		final List<UserChatStats> userStatList = new ArrayList<>();
		Set<String> visitedVideoIds = new HashSet<String>(); 
		for (ActorRef ar : actorSet) {
			final String videoId = channelVsVideoId.get(ar);
			if(visitedVideoIds.add(videoId)) {
				List<Document> countMessagesByUser = ChatMessageUtil.getCountChatMessagesForVideoByUser(videoId);
				if (countMessagesByUser != null && !countMessagesByUser.isEmpty()) {
					countMessagesByUser.forEach((doc) -> {
						userStatList.add(new UserChatStats(doc.getString("_id"), doc.getString("sender"),
								doc.getInteger("num_messages").intValue(), videoId));
					});
				}
			}
			// userMessagesInVideo.put(string, userStatList);
		}

		Collections.sort(userStatList, (c1, c2) -> {
			return c2.getNumMessages() - c1.getNumMessages();
		});

		if (!statList.isEmpty()) {
			Collections.sort(statList, (c1, c2) -> {
				return c2.getNumMessages() - c1.getNumMessages();
			});
		}

		Map<Object, Object> stats = new HashMap<>();
		stats.put("type", "stats");
		stats.put("stats", statList);
		stats.put("userStats", userStatList);
		stats.put("rollingStats", rtcsList);

		actorSet = channelVsVideoId.keySet();
		for (ActorRef actors : actorSet) {
			actors.tell(Json.toJson(stats), null);
		}
	}

	private long countMessagesPerInterval(final Set<ChatMessage> messages, long interval) {
		long now = new Date().getTime();

		long value = messages.stream().filter(d -> {
			return now - d.getTime().getTime() < interval;
		}).count();

		return value;
	}
}
