package controllers;

import static utils.UrlsAndConstants.CHANNEL_URL;
import static utils.UrlsAndConstants.API_KEY;

import static utils.IdTokenVerifier.verifyIdToken;
import static utils.UserProfileUtil.createUserProfile;
import static utils.UserProfileUtil.getUserProfileDocument;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.bson.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import akka.stream.Materializer;
import play.libs.ws.WSClient;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class AuthenticationController extends Controller {

	@Inject
	WSClient ws;
	@Inject
	Materializer materializer;

	@BodyParser.Of(BodyParser.Json.class)
	public Result login() {
		System.out.println("AuthenticationController.login()");
		JsonNode node = request().body().asJson();

		String idTokenStr = node.findPath("idToken").textValue();
		if (idTokenStr == null || idTokenStr.isEmpty()) {
			return badRequest("idToken is required");
		}

		GoogleIdToken idToken = verifyIdToken(idTokenStr);

		if (idToken == null) {
			return unauthorized("Id Token is invalid");
		}

		Payload payload = idToken.getPayload();
		final String email = payload.getEmail();

		Document userDoc = getUserProfileDocument(email);

		if (userDoc == null) {
			// get the channel owned by the user.
			final String accessToken = node.findPath("accessToken").textValue();
			System.out.println("accessToken " + accessToken);
			Document newUser = createUserProfile(idToken, getChannelId(accessToken));
			System.out.println(newUser.toJson());

			return created(newUser.toJson());
		} else {
			return ok(userDoc.toJson());
		}
	}

	private String getChannelId(final String accessToken) {
		String channelId = null;

		CompletionStage<JsonNode> futureResponse = ws.url(CHANNEL_URL).addQueryParameter("mine", "true")
				.addQueryParameter("part", "id").addQueryParameter("key", API_KEY).addQueryParameter("maxResults", "1")
				.addHeader("Authorization", "Bearer " + accessToken).get().thenApply(r -> r.asJson());
		try {
			JsonNode json = futureResponse.toCompletableFuture().get();
			System.out.println("json " + json.toString());
			channelId = json.get("items").get(0).get("id").textValue();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return channelId;
	}
}
