package actors;

import static utils.UrlsAndConstants.API_KEY;
import static utils.UrlsAndConstants.FETCH_YTLIVE_MESSAGES;
import static utils.UrlsAndConstants.LIVE_CHATMESSAGES_URL;
import static utils.ChatMessageUtil.insertNewChatMessage;
import static utils.ChatMessageUtil.getChatHistory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.bson.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import chat.ChatBroadCastManager;
import chat.ChatStatsManager;
import chat.YTLiveChatManager;
import model.ChatMessage;
import play.libs.Json;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

public class LiveChatMessageActor extends AbstractActor {

	public static Props props(ActorRef out, WSClient ws) {
		return Props.create(LiveChatMessageActor.class, out, ws);
	}

	private final ActorRef out;
	private final WSClient ws;
	
	public LiveChatMessageActor(ActorRef out, WSClient ws) {
		this.out = out;
		this.ws = ws;
	}

	@Override
	public void postStop() throws Exception {
		ChatBroadCastManager.getInstance().removeActor(this.out);
		ChatStatsManager.getInstance().removeChannel(this.out);
		YTLiveChatManager.getInstance().removeActor(this.out);
		super.postStop();
	}

	@Override
	public Receive createReceive() {
		System.out.println("LiveChatMessageActor.createReceive() " + self());
		return receiveBuilder().match(JsonNode.class, message -> {
			// System.out.println(new Date().getTime() + ": message " + message);

			final JsonNode type = message.get("type");
			if (type != null) {
				if (type.textValue().equals("heartbeat")) {
					// Do nothing
					// out.tell(message, self());

				} else if (type.textValue().equals("message")) {
					ObjectMapper mapper = new ObjectMapper();
					ChatMessage cm = Json.fromJson(message, ChatMessage.class);
					cm.setTime(new Date());

					System.out.println("cm " + cm);

					insertNewChatMessage(cm);

					ChatStatsManager.getInstance().addMessage(cm);
					ChatBroadCastManager.getInstance().broadCastMessage(cm);
					
					pushChatMessageToYTLive(cm, message.get("accessToken").textValue());

					// out.tell(Json.toJson(message), self());
				} else if (type.textValue().equals("join")) {
					ObjectMapper mapper = new ObjectMapper();
					ChatMessage cm = Json.fromJson(message, ChatMessage.class);
					cm.setTime(new Date());

					ChatBroadCastManager.getInstance().addActor(cm.getVideoId(), out);
					ChatStatsManager.getInstance().addChannel(out, cm.getVideoId());
					
					
					if(FETCH_YTLIVE_MESSAGES) {
						YTLiveChatManager.getInstance().setWSClient(ws);
						YTLiveChatManager.getInstance().addActor(cm, out);
					}

					List<Document> chatHistory = getChatHistory(cm.getVideoId());
					Map<String, Object> response = new HashMap<>();
					response.put("type", "history");
					response.put("history", chatHistory);
					System.out.println("chatHistory.size() " + chatHistory.size());
					out.tell(Json.toJson(response), null);
				}
			}

		}).build();
	}

	private void pushChatMessageToYTLive(final ChatMessage cm, final String accessToken) {
		Map<String, Object> textMessageDetails = new HashMap<>();
		textMessageDetails.put("messageText", cm.getMessage());

		Map<String, Object> snippet = new HashMap<>();
		snippet.put("type", "textMessageEvent");
		snippet.put("textMessageDetails", textMessageDetails);
		snippet.put("liveChatId", cm.getLiveChatId());

		Map<String, Object> postBody = new HashMap<>();
		postBody.put("snippet", snippet);
		
		CompletionStage<JsonNode> futureResponse = ws.url(LIVE_CHATMESSAGES_URL).addQueryParameter("part", "snippet")
				.addHeader("Authorization", "Bearer " + accessToken)
				.post(Json.toJson(postBody)).thenApply(r -> r.asJson());
		try {
			JsonNode json = futureResponse.toCompletableFuture().get();
			System.out.println("response json " + json);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
