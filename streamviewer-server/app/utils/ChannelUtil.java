package utils;

import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.HashSet;
import java.util.Set;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import db.MongoManager;

public final class ChannelUtil {

	public static Set<String> getAllUserChannels() {
		MongoCollection<Document> userCollection = MongoManager.getDatabase()
				.getCollection(MongoManager.COLLECTION_USERS);

		FindIterable<Document> iter = userCollection.find().projection(fields(include("channelId"), excludeId()));

		Set<String> allChannels = new HashSet<String>();
		for (Document doc : iter) {
			allChannels.add(doc.getString("channelId"));
		}
		
		return allChannels;
	}
}
