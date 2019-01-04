package controllers;

import static utils.ChannelUtil.getAllUserChannels;
import static utils.LiveStreamsUtil.getMyLiveStreams;
import static utils.LiveStreamsUtil.getMySubscribedChannels;
import static utils.LiveStreamsUtil.getOtherLiveStreams;
import static utils.LiveStreamsUtil.getPublicLiveStreams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import action.AccessTokenVerifierAction;
import akka.stream.Materializer;
import model.LiveStream;
import model.Subscription;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Returns the live streams from YT live API.
 * 
 * @author dharani
 *
 */
public class LiveStreamsController extends Controller {

	@Inject
	WSClient ws;
	@Inject
	Materializer materializer;

	@Security.Authenticated(AccessTokenVerifierAction.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result liveStreams() {
		System.out.println("LiveStreamsController.liveStreams()");
		JsonNode json = request().body().asJson();
		final String accessToken = json.get("accessToken").textValue();
		
		Map<String, Object> response = new HashMap<String, Object>();

		// Get all the subscribed channels of the user.
		List<Subscription> mySubscriptions = getMySubscribedChannels(ws, accessToken);
		System.out.println("mySubscriptions " + mySubscriptions);

		final Set<String> allUserChannels = getAllUserChannels();

		boolean noSubscriptions = false;
		if (!isSubscribedToOurUserChannels(mySubscriptions, allUserChannels)) {
			noSubscriptions = true;
			response.put("noSubscriptions", noSubscriptions);
			// This user is totally new to YouTube and does not have any subscriptions.
		}

		// Get the public live streams - free to watch without subscriptions
		List<LiveStream> publicLiveStreams = getPublicLiveStreams(ws, accessToken);
		System.out.println("publicLiveStreams " + publicLiveStreams);

		// Your own live streaming - in case you are streaming from studio.youtube.com or youtube mobile app.
		List<LiveStream> myLiveStreams = getMyLiveStreams(ws, allUserChannels, accessToken);
		System.out.println("myLiveStreams " + myLiveStreams);
		
		// Any other users registered with SV is broadcasting currently. 
		// We will get data only if the broadcasting is public
		List<LiveStream> otherSVUsersStream = getOtherLiveStreams(ws, allUserChannels, accessToken);
		System.out.println("otherSVUsersStream " + otherSVUsersStream);

		if (myLiveStreams == null || myLiveStreams.isEmpty()) {
			System.out.println("User does not have live streaming enabled or does not stream currently");

			// User don't have any live streaming from his account. See whether any other
			// user is broadcasting live.

			if (otherSVUsersStream.isEmpty()) {
				// None of the SV users are broadcasting currently, hence he can watch only
				// public streams
				response.put("warn", "None of the SV users are broadcasting currently.");
				response.put("liveStreams", publicLiveStreams);
			} else {
				// The user will see SV user broadcast followed by public
				otherSVUsersStream.addAll(publicLiveStreams);
				response.put("liveStreams", otherSVUsersStream);
			}
			
		} else {
			// User itself is broadcasting currently. We will also send other public live streams along
			myLiveStreams.addAll(publicLiveStreams);
			response.put("liveStreams", myLiveStreams);
			response.put("self", true);
		}

//		if (noSubscriptions && myLiveStreams.isEmpty()) {
//			response.put("warn", "User is not subscribed to any of the application user's channel");
//			response.put("liveStreams", publicLiveStreams);
//			return ok(Json.toJson(response));
//		}
		return ok(Json.toJson(response));
	}

	private boolean isSubscribedToOurUserChannels(final List<Subscription> mySubscriptions,
			final Set<String> allOurUserChannels) {
		Predicate<Subscription> matchChannel = sub -> allOurUserChannels.contains(sub.getChannelId());
		return mySubscriptions.stream().filter(matchChannel).count() > 0;
	}
}
