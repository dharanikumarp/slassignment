package controllers;

import javax.inject.Inject;

import actors.StatisticsActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.WebSocket;

public class StatisticsController extends Controller {

	private final ActorSystem actorSystem;
	private final Materializer materializer;
	
	@Inject
	public StatisticsController(ActorSystem actorSystem, Materializer materializer) {
		this.actorSystem = actorSystem;
		this.materializer = materializer;
	}
	
	public WebSocket stats() {
		return WebSocket.Json
				.accept(request -> ActorFlow.actorRef(StatisticsActor::props, actorSystem, materializer)
		);
	}
}
