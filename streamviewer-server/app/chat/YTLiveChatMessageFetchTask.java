package chat;

import static utils.UrlsAndConstants.API_KEY;

import static utils.UrlsAndConstants.LIVE_CHATMESSAGES_URL;
import static utils.UserProfileUtil.getAllUsers;
import static utils.UrlsAndConstants.FILTER_SV_USERS_IN_YTLIVE_MESSAGES;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.bson.Document;

import com.fasterxml.jackson.databind.JsonNode;

import model.ChatMessage;
import play.libs.ws.WSClient;
import utils.ChatMessageUtil;

public class YTLiveChatMessageFetchTask extends TimerTask {

	private Timer timer;
	private WSClient ws;
	private String liveChatId;
	private String videoId;
	private String title;
	private String description;

	// The token that can be used as the value of the pageToken parameter to
	// retrieve the next page in the result set.
	private String nextPageToken;

	// The amount of time, in milliseconds, that the client should wait before
	// polling again for new live chat messages.
	private int pollingIntervalMillis = 0;

	public YTLiveChatMessageFetchTask(final Timer timer, final WSClient ws, final String liveChatId,
			final String videoId, final String title, final String description, final String nextPageToken,
			int pollingIntervalMillis) {
		this.timer = timer;
		this.ws = ws;
		this.liveChatId = liveChatId;
		this.videoId = videoId;
		this.title = title;
		this.description = description;
		this.nextPageToken = nextPageToken;
		this.pollingIntervalMillis = pollingIntervalMillis;
	}

	@Override
	public void run() {
		boolean firstRequest = this.nextPageToken == null || this.nextPageToken.isEmpty();

		List<Document> allUsers = getAllUsers();
		Map<String, Document> channelIdVsUsers = new HashMap<>();

		allUsers.forEach(d -> {
			String channelId = d.getString("channelId");
			channelIdVsUsers.put(channelId, d);
		});

		CompletionStage<JsonNode> futureResponse = null;

		if (nextPageToken != null && !nextPageToken.isEmpty()) {
			futureResponse = ws.url(LIVE_CHATMESSAGES_URL).addQueryParameter("liveChatId", liveChatId)
					.addQueryParameter("part", "snippet,authorDetails").addQueryParameter("key", API_KEY)
					.addQueryParameter("pageToken", nextPageToken).get().thenApply(r -> r.asJson());
		} else {
			futureResponse = ws.url(LIVE_CHATMESSAGES_URL).addQueryParameter("liveChatId", this.liveChatId)
					.addQueryParameter("part", "snippet,authorDetails").addQueryParameter("key", API_KEY).get()
					.thenApply(r -> r.asJson());
		}

		try {
			long currentTimeInMillis = Instant.now().toEpochMilli();
			long previousPollTime = currentTimeInMillis - this.pollingIntervalMillis;

			JsonNode json = futureResponse.toCompletableFuture().get();

			this.nextPageToken = json.get("nextPageToken").textValue();
			this.pollingIntervalMillis = json.get("pollingIntervalMillis").intValue();

			int numResults = json.get("pageInfo").get("resultsPerPage").asInt();
			System.out.println("numResults " + numResults);

			for (int i = 0; i < numResults; i++) {
				JsonNode jn = json.get("items").get(i);

				JsonNode snippet = jn.get("snippet");
				JsonNode authorDetails = jn.get("authorDetails");
				String authorChannelId = null;

				Instant publishedAt = Instant.parse(snippet.get("publishedAt").textValue());
				
				if(!snippet.has("hasDisplayContent") || !snippet.get("hasDisplayContent").asBoolean()) {
					System.out.println("skipping non displayable content " + snippet);
					continue;
				}
				
				if ((snippet.has("hasDisplayContent") && snippet.get("hasDisplayContent").asBoolean())
						&& !authorDetails.has("channelId") && snippet.has("textMessageDetails")
						&& snippet.get("textMessageDetails").has("messageText")) {
					continue;
				} else {
					authorChannelId = authorDetails.get("channelId").textValue();
				}

				ChatMessage cm = new ChatMessage();
				cm.setType("message");
				cm.setLiveChatId(liveChatId);
				cm.setTitle(title);
				cm.setVideoId(videoId);
				cm.setDescription(description);
				cm.setTime(new Date(publishedAt.toEpochMilli()));

				// Some messages are missing this field. Ignore them.
				JsonNode tmd = snippet.get("textMessageDetails");
				if (tmd != null && tmd.has("messageText")) {
					cm.setMessage(tmd.get("messageText").textValue());
				} else {
					continue;
				}

				if (FILTER_SV_USERS_IN_YTLIVE_MESSAGES) {
					Document user = channelIdVsUsers.get(authorChannelId);

					if (user != null) {
						System.out.println("Found user for channelId " + authorChannelId);
						final String gmail = user.getString("email");

						// Get all the messages from the last 10 seconds or previousPollTime till now
						Date from = firstRequest ? new Date(Instant.now().toEpochMilli() - 10000)
								: new Date(previousPollTime - 10000);
						List<Document> chatMessages = ChatMessageUtil.getChatMessagesWithinInterval(videoId, gmail,
								from, new Date());

						boolean skip = false;
						for (Document cmDoc : chatMessages) {
							if (cmDoc.getString("message").equals(cm.getMessage())) {
								skip = true;
								break;
							}
						}

						if (skip) {
							System.out.println("skipping message as it was persisted already");
							continue;
						}

						// process this message
						cm.setSender(user.getString("name"));
						cm.setGmail(gmail);
						cm.setProfileImageUrl(user.getString("pictureUrl"));
						cm.setGoogleUserId(user.getString("googleUserId"));
					} else {
						continue;
					}

				} else {
					cm.setSender(authorDetails.get("displayName").textValue());

					// We will use channelId as gmail & googleUserId - we cannot obtain gmail here
					cm.setGmail(authorChannelId);
					cm.setGoogleUserId(authorChannelId);

					cm.setProfileImageUrl(authorDetails.get("profileImageUrl").textValue());
				}
				ChatMessageUtil.insertNewChatMessage(cm);
				ChatStatsManager.getInstance().addMessage(cm);

				if (FILTER_SV_USERS_IN_YTLIVE_MESSAGES) {
					ChatBroadCastManager.getInstance().broadCastMessage(cm);
				} else {
					// Delay to prevent AKKA buffer overflow error.
					ChatBroadCastManager.getInstance().broadCastMessage(cm, 500L);
				}
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		timer.schedule(new YTLiveChatMessageFetchTask(timer, ws, liveChatId, videoId, title, description,
				this.nextPageToken, this.pollingIntervalMillis), this.pollingIntervalMillis);
	}
}
