package utils;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import db.MongoManager;

public final class UserProfileUtil {

	public static Document createUserProfile(final GoogleIdToken idToken, final String channelId) {
		if (idToken == null) {
			return null;
		}

		Payload payload = idToken.getPayload();
		final String email = payload.getEmail();
		if (checkIfEmailExists(email)) {
			return getUserProfileDocument(email);
		}

		MongoCollection<Document> userCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_USERS);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("email", payload.getEmail());
		result.put("name", (String) payload.get("name"));
		result.put("pictureUrl", (String) payload.get("picture"));
		result.put("givenName", (String) payload.get("given_name"));
		result.put("googleUserId", payload.getSubject());
		result.put("channelId", channelId);

		Document res = new Document(result);
		userCollection.insertOne(res);
		System.out.println("creating new user " + payload.getEmail());
		return res;
	}

	public static boolean checkIfEmailExists(final String emailId) {
		MongoCollection<Document> userCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_USERS);
		long count = userCollection.countDocuments(new BsonDocument("email", new BsonString(emailId)));

		return count > 0;
	}

	public static Document getUserProfileDocument(final String emailId) {
		MongoCollection<Document> userCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_USERS);

		Document userProfileDoc = userCollection.find(eq("email", emailId)).first();
		return userProfileDoc;
	}

	public static List<Document> getAllUsers() {
		MongoCollection<Document> userCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_USERS);

		FindIterable<Document> iterable = userCollection.find();

		final List<Document> allUsers = new ArrayList<Document>();
		Consumer<Document> consumer = (doc) -> {
			allUsers.add(doc);
		};
		iterable.forEach(consumer);

		return allUsers;
	}
}
