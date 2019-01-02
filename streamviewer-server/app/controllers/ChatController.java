package controllers;

import javax.inject.Inject;

import actors.LiveChatMessageActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.WebSocket;
import play.libs.ws.WSClient;

public class ChatController extends Controller {

	private final ActorSystem actorSystem;
	private final Materializer materializer;
	private final WSClient ws;

	@Inject
	private ChatController(ActorSystem actorSystem, Materializer materializer, WSClient ws) {
		this.actorSystem = actorSystem;
		this.materializer = materializer;
		this.ws = ws;
	}

	public WebSocket chat() {
		return WebSocket.Json
				.accept(request -> ActorFlow.actorRef(out -> LiveChatMessageActor.props(out, ws), actorSystem, materializer)
		);
	}
}
