package chat;

import static utils.UrlsAndConstants.API_KEY;
import static utils.UrlsAndConstants.LIVE_CHATMESSAGES_URL;
import static utils.UserProfileUtil.getAllUsers;
import static utils.UrlsAndConstants.FILTER_SV_USERS_IN_YTLIVE_MESSAGES;

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

public class YTLiveChatMessageFetchTask extends TimerTask {

	private Timer timer;
	private WSClient ws;
	private String liveChatId;
	private String videoId;
	private String title;
	private String description;

	public YTLiveChatMessageFetchTask(final Timer timer, final WSClient ws, final String liveChatId,
			final String videoId, final String title, final String description) {
		this.timer = timer;
		this.ws = ws;
		this.liveChatId = liveChatId;
		this.videoId = videoId;
		this.title = title;
		this.description = description;
	}

	// The token that can be used as the value of the pageToken parameter to
	// retrieve the next page in the result set.
	private String nextPageToken;

	// The amount of time, in milliseconds, that the client should wait before
	// polling again for new live chat messages.
	private int pollingIntervalMillis = 0;

	@Override
	public void run() {

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
			JsonNode json = futureResponse.toCompletableFuture().get();
			System.out.println("response json " + json);

			nextPageToken = json.get("nextPageToken").textValue();
			pollingIntervalMillis = json.get("pollingIntervalMillis").intValue();

			for (JsonNode jn : json.get("items")) {
				JsonNode snippet = jn.get("snippet");
				JsonNode authorDetails = jn.get("authorDetails");

				ChatMessage cm = new ChatMessage();
				cm.setLiveChatId(liveChatId);
				cm.setTitle(title);
				cm.setVideoId(videoId);
				cm.setDescription(description);
				
				cm.setMessage(snippet.get("textMessageDetails").get("messageText").textValue());

				boolean skip = false;

				if (FILTER_SV_USERS_IN_YTLIVE_MESSAGES) {
					String authorChannelId = authorDetails.get("authorChannelId").textValue();
					// Date publishedDate =
					Document user = channelIdVsUsers.get(authorChannelId);

					if (user != null) {
						// process this message
						final String gmail = user.getString("email");

						// long currentTimeInMillis = new Date().getTime();
						// Date to = new Date(currentTimeInMillis);
						// long from = pollingIntervalMillis != 0 ? currentTimeInMillis -
						// pollingIntervalMillis : currentTimeInMillis - 1000;

						// List<Document> messages = getChatMessagesWithinInterval(videoId, gmail, new
						// Date(from), to);

						

						cm.setSender(user.getString("name"));
						cm.setGmail(gmail);
						cm.setProfileImageUrl(user.getString("pictureUrl"));
						cm.setGoogleUserId(user.getString("googleUserId"));
					} else {
						skip = true;
					}

				} else {
					cm.setSender(authorDetails.get("displayName").textValue());
					cm.setGmail(authorDetails.get("channelId").textValue());
					cm.setProfileImageUrl(authorDetails.get("profileImageUrl").textValue());
					cm.setGoogleUserId(authorDetails.get("channelId").textValue());
				}

				if (!skip) {
					ChatBroadCastManager.getInstance().broadCastMessage(cm);
				}
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		timer.schedule(new YTLiveChatMessageFetchTask(timer, ws, liveChatId, videoId, title, description),
				pollingIntervalMillis);
	}
}
