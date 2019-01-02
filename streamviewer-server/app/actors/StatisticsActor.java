package actors;

import static utils.ChatMessageUtil.getAllUserMessageStats;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bson.Document;

import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Json;
import utils.ChatMessageUtil;

/**
 * 
 * @author dharani
 *
 */
public class StatisticsActor extends AbstractActor {

	public static Props props(ActorRef out) {
		return Props.create(StatisticsActor.class, out);
	}

	private final ActorRef out;
	private Timer timer;

	public StatisticsActor(ActorRef out) {
		this.out = out;
		timer = new Timer();
	}

	@Override
	public void postStop() throws Exception {
		System.out.println("StatisticsActor.postStop() " + out);
		timer.cancel();
	}

	@Override
	public Receive createReceive() {
		System.out.println("StatisticsActor.createReceive() " + self());
		return receiveBuilder().match(JsonNode.class, message -> {
			long delay = 1000L;
			long interval = 2000;
			timer.scheduleAtFixedRate(new StatsTimerTask(this.out), delay, interval);

		}).build();
	}

	class StatsTimerTask extends TimerTask {
		private ActorRef out;

		public StatsTimerTask(final ActorRef out) {
			this.out = out;
		}

		@Override
		public void run() {
			List<Document> userStats = getAllUserMessageStats();
			out.tell(Json.toJson(userStats), self());
		}
	}
}
