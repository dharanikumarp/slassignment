package utils;

import static utils.UrlsAndConstants.API_KEY;

import static utils.UrlsAndConstants.LIVEBROADCAST_URL;
import static utils.UrlsAndConstants.MAX_RESULTS;
import static utils.UrlsAndConstants.SEARCH_TERM;
import static utils.UrlsAndConstants.SEARCH_URL;
import static utils.UrlsAndConstants.SUBSCRIPTION_URL;
import static utils.UrlsAndConstants.VIDEOS_URL;
import static utils.UrlsAndConstants.SHOW_PUBLIC_BROADCASTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.JsonNode;

import model.LiveStream;
import model.Subscription;
import model.Thumbnail;

import play.libs.ws.WSClient;

public final class LiveStreamsUtil {

	public static List<Subscription> getMySubscribedChannels(final WSClient ws, final String accessToken) {
		List<Subscription> mySubscriptions = null;
		CompletionStage<JsonNode> futureResponse = ws.url(SUBSCRIPTION_URL).addQueryParameter("mine", "true")
				.addQueryParameter("part", "snippet").addQueryParameter("key", API_KEY)
				.addQueryParameter("maxResults", "50").addHeader("Authorization", "Bearer " + accessToken).get()
				.thenApply(r -> r.asJson());
		try {
			JsonNode json = futureResponse.toCompletableFuture().get();

			System.out.println("json " + json.toString());

			mySubscriptions = new ArrayList<Subscription>();

			for (JsonNode jn : json.get("items")) {
				Subscription sub = new Subscription();
				sub.setId(jn.get("id").textValue());
				JsonNode jnSnippet = jn.get("snippet");
				sub.setChannelId(jnSnippet.get("resourceId").get("channelId").textValue());
				sub.setTitle(jnSnippet.get("title").textValue());
				sub.setDescription(jnSnippet.get("description").textValue());

				mySubscriptions.add(sub);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return mySubscriptions;
	}

	public static List<LiveStream> getMyLiveStreams(final WSClient ws, final Set<String> allOurChannels,
			final String accessToken) {
		List<LiveStream> myLiveStreams = null;

		CompletionStage<JsonNode> futureResponse = ws.url(LIVEBROADCAST_URL).addQueryParameter("part", "snippet")
				.addQueryParameter("key", API_KEY).addQueryParameter("maxResults", "" + MAX_RESULTS)
				.addQueryParameter("broadcastStatus", "active").addHeader("Authorization", "Bearer " + accessToken)
				.get().thenApply(r -> r.asJson());

		try {
			JsonNode json = futureResponse.toCompletableFuture().get();

			if (json.get("error") == null) {
				int numResults = json.get("pageInfo").get("totalResults").asInt();

				myLiveStreams = new ArrayList<LiveStream>();
				for (int i = 0; i < numResults; i++) {
					JsonNode jn = json.get("items").get(i);

					LiveStream ls = new LiveStream();
					ls.setVideoId(jn.get("id").textValue());
					ls.setChannelId(jn.get("snippet").get("channelId").textValue());
					ls.setTitle(jn.get("snippet").get("title").textValue());
					ls.setDescription(jn.get("snippet").get("description").textValue());
					ls.setLiveChatId(getLiveChatId(ws, jn.get("id").textValue(), accessToken));
					ls.setThumbnails(getThumbnails(jn.get("snippet").get("thumbnails")));
					myLiveStreams.add(ls);
				}
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return myLiveStreams;
	}

	public static List<LiveStream> getOtherLiveStreams(final WSClient ws, final Set<String> allOurChannels, final String accessToken) {
		List<LiveStream> liveStreams = new ArrayList<LiveStream>();

		for (String channelId : allOurChannels) {
			LiveStream ls = getLiveStreamForChannel(ws, channelId, accessToken);
			if (ls != null) {
				liveStreams.add(ls);
			}
		}
		return liveStreams;
	}

	public static LiveStream getLiveStreamForChannel(final WSClient ws, final String channelId,
			final String accessToken) {
		System.out.println("LiveStreamsController.getLiveStreamForChannel() " + channelId);
		LiveStream ls = null;
		CompletionStage<JsonNode> futureResponse = ws.url(SEARCH_URL).addQueryParameter("part", "snippet")
				.addQueryParameter("key", API_KEY).addQueryParameter("maxResults", "" + MAX_RESULTS)
				.addQueryParameter("type", "video").addQueryParameter("eventType", "live")
				.addQueryParameter("channelId", channelId)
				// .addHeader("Authorization", "Bearer " + accessToken)
				.get().thenApply(r -> r.asJson());

		try {
			JsonNode json = futureResponse.toCompletableFuture().get();
			System.out.println("json " + json.toString());

			int numResults = json.get("pageInfo").get("totalResults").asInt();

			if (numResults != 0) {
				JsonNode jn = json.get("items").get(0);
				ls = getLiveStream(ws, jn, accessToken);
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return ls;
	}

	public static String getLiveChatId(final WSClient ws, final String videoId, final String accessToken) {
		System.out.println("LiveStreamsController.getLiveChatId(), videoId " + videoId);
		String liveChatId = null;

		CompletionStage<JsonNode> futureResponse = ws.url(VIDEOS_URL).addQueryParameter("key", API_KEY)
				.addQueryParameter("part", "liveStreamingDetails").addQueryParameter("id", videoId)
				.addHeader("Authorization", "Bearer " + accessToken).get().thenApply(r -> r.asJson());
		try {
			JsonNode json = futureResponse.toCompletableFuture().get();
			System.out.println("json " + json.toString());
			JsonNode node = json.get("items").get(0).get("liveStreamingDetails");
			if (node != null && node.has("activeLiveChatId")) {
				liveChatId = node.get("activeLiveChatId").textValue();
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return liveChatId;
	}

	private static Map<String, Thumbnail> getThumbnails(final JsonNode node) {
		Map<String, Thumbnail> thumbnails = new HashMap<String, Thumbnail>();
		thumbnails.put("default", getThumbnail(node.get("default")));
		thumbnails.put("medium", getThumbnail(node.get("medium")));
		thumbnails.put("high", getThumbnail(node.get("high")));
		return thumbnails;
	}

	/**
	 * Return the live broadcasts from YouTube live - Free to watch.
	 */
	public static List<LiveStream> getPublicLiveStreams(final WSClient ws, final String accessToken) {
		List<LiveStream> liveStreams = new ArrayList<>();
		
		if(SHOW_PUBLIC_BROADCASTS) {
			CompletionStage<JsonNode> futureResponse = ws.url(SEARCH_URL).addQueryParameter("part", "snippet")
					.addQueryParameter("key", API_KEY).addQueryParameter("maxResults", "" + MAX_RESULTS)
					.addQueryParameter("type", "video").addQueryParameter("eventType", "live")
					.addQueryParameter("q", SEARCH_TERM)
					// .addHeader("Authorization", "Bearer " + accessToken)
					.get().thenApply(r -> r.asJson());

			try {
				JsonNode json = futureResponse.toCompletableFuture().get();
				System.out.println("json " + json.toString());

				int numResults = json.get("pageInfo").get("totalResults").asInt();

				if (numResults != 0) {
					for (int i = 0; i < MAX_RESULTS; i++) {

						JsonNode jn = json.get("items").get(i);
						LiveStream ls = getLiveStream(ws, jn, accessToken);
						if (ls != null) {
							liveStreams.add(ls);
						}
					}
				}

			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return liveStreams;
	}

	public static LiveStream getLiveStream(final WSClient ws, final JsonNode jn, final String accessToken) {
		LiveStream ls = null;

		final String videoId = jn.get("id").get("videoId").textValue();
		final String liveChatId = getLiveChatId(ws, videoId, accessToken);

		if (liveChatId != null && !liveChatId.isEmpty()) {
			ls = new LiveStream();

			ls.setVideoId(videoId);
			ls.setChannelId(jn.get("snippet").get("channelId").textValue());
			ls.setTitle(jn.get("snippet").get("title").textValue());
			ls.setDescription(jn.get("snippet").get("description").textValue());
			ls.setChannelTitle(jn.get("snippet").get("channelTitle").textValue());
			ls.setThumbnails(getThumbnails(jn.get("snippet").get("thumbnails")));
			ls.setLiveChatId(liveChatId);
		}
		return ls;
	}

	private static Thumbnail getThumbnail(final JsonNode node) {
		return new Thumbnail(node.get("url").textValue(), node.get("width").asInt(), node.get("height").asInt());
	}
}
