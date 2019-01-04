package utils;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

import db.MongoManager;
import model.ChatMessage;

public final class ChatMessageUtil {

	private static final int MESSAGES_LIMIT = 100000;

	public static List<Document> getChatMessages(final String emailId) {
		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		final List<Document> messages = new ArrayList<Document>();
		Consumer<Document> consumer = (doc) -> {
			messages.add(doc);
		};
		chatCollection.find(eq("gmail", emailId)).limit(MESSAGES_LIMIT).forEach(consumer);
		return messages;
	}

	public static List<Document> getAllChatMessages() {
		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		final List<Document> messages = new ArrayList<Document>();
		Consumer<Document> consumer = (doc) -> {
			messages.add(doc);
		};
		chatCollection.find().limit(MESSAGES_LIMIT).forEach(consumer);
		return messages;
	}

	public static List<Document> getCountChatMessagesForVideoByUser(final String videoId) {
		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		AggregateIterable<Document> iterable = chatCollection
				.aggregate(Arrays.asList(new Document("$match", new Document("videoId", videoId)),
						new Document("$group",
								new Document("_id", "$gmail").append("num_messages", new Document("$sum", 1))
										.append("sender", new Document("$first", "$sender")))));

		final List<Document> messages = new ArrayList<Document>();
		Consumer<Document> consumer = (doc) -> {
			messages.add(doc);
		};
		iterable.forEach(consumer);
		return messages;
	}

	public static void insertNewChatMessage(final ChatMessage cm) {
		Document cmDoc = new Document();
		cmDoc.put("videoId", cm.getVideoId());
		cmDoc.put("gmail", cm.getGmail());
		cmDoc.put("googleUserId", cm.getGoogleUserId());
		cmDoc.put("message", cm.getMessage());
		cmDoc.put("time", cm.getTime());
		cmDoc.put("sender", cm.getSender());
		cmDoc.put("title", cm.getTitle());
		cmDoc.put("description", cm.getDescription());
		cmDoc.put("profileImageUrl", cm.getProfileImageUrl());

		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		chatCollection.insertOne(cmDoc);
	}

	public static List<Document> getAllUserMessageStats() {
		List<Document> mostActiveVideoWithCount = getMostActiveVideoWithCount();
		List<Document> userMessageCount = getCountOfAllMessages();

		final List<Document> finalList = new ArrayList<>();

		for (Document d1 : mostActiveVideoWithCount) {

			for (Document d2 : userMessageCount) {
				if (d2.getString("gmail").equals(d1.getString("_id"))) {
					Document dNew = new Document(d1);
					dNew.put("gmail", d2.getString("gmail"));
					dNew.put("totalMessages", d2.getInteger("totalMessages"));
					finalList.add(dNew);
				}
			}
		}

		return finalList;
	}

	private static List<Document> getMostActiveVideoWithCount() {
		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		// MongoDB aggregate query

		// {
		// '$group': {'_id': {'gmail': '$gmail', 'videoId': '$videoId'}, 'sender':
		// {'$first': '$sender'}, 'count': {'$sum': 1}}
		// },
		// {
		// '$sort': {'count':-1}
		// },
		// {
		// '$group': {'_id': '$_id.gmail', 'videoId': {'$first': '$_id.videoId'},
		// 'sender': {'$first': '$sender'},
		// 'maxCount': {'$first': '$count'}}
		// }

		AggregateIterable<Document> iterable = chatCollection.aggregate(Arrays.asList(new Document("$group",
				new Document("_id", new Document("gmail", "$gmail").append("videoId", "$videoId"))
						.append("count", new Document("$sum", 1)).append("sender", new Document("$first", "$sender"))
						.append("title", new Document("$first", "$title")))

				, new Document("$sort", new Document("count", -1)),
				new Document("$group",
						new Document("_id", "$_id.gmail")
								.append("mostActiveVideo", new Document("$first", "$_id.videoId"))
								.append("sender", new Document("$first", "$sender"))
								.append("title", new Document("$first", "$title"))
								.append("mostActiveVideoNumMsgs", new Document("$first", "$count")))));

		final List<Document> countUserVideos = new ArrayList<Document>();
		Consumer<Document> consumer = (doc) -> {
			countUserVideos.add(doc);
		};
		iterable.forEach(consumer);

		return countUserVideos;
	}

	private static List<Document> getCountOfAllMessages() {
		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		AggregateIterable<Document> iterable = chatCollection.aggregate(Arrays.asList(new Document("$group",
				new Document("_id", "$gmail").append("totalMessages", new Document("$sum", 1)))));

		final List<Document> countUserMessages = new ArrayList<Document>();
		Consumer<Document> consumer = (doc) -> {
			Document docNew = new Document();
			docNew.put("gmail", doc.getString("_id"));
			docNew.put("totalMessages", doc.getInteger("totalMessages"));

			countUserMessages.add(docNew);
		};
		iterable.forEach(consumer);

		return countUserMessages;
	}

	public static List<Document> getChatHistory(final String videoId) {
		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		List<Document> history = new ArrayList<>();
		Consumer<Document> consumer = (doc) -> {
			doc.remove("_id");
			history.add(doc);
		};
		chatCollection.find(eq("videoId", videoId)).sort(new Document("time", -1)).limit(MESSAGES_LIMIT)
				.forEach(consumer);
		return history;
	}

	public static List<Document> getChatMessagesWithinInterval(final String videoId, final String gmail, Date from,
			Date to) {
		MongoCollection<Document> chatCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_CHATS);

		List<Document> list = new ArrayList<>();
		Consumer<Document> consumer = (doc) -> {
			list.add(doc);
		};

		chatCollection.find(and(eq("videoId", videoId), eq("gmail", gmail), gte("time", from), lte("time", to)))
				.sort(new Document("time", -1)).forEach(consumer);
		return list;
	}
}