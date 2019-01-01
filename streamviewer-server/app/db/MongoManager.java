package db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public final class MongoManager {

	// mongodb://<dbuser>:<dbpassword>@ds243084.mlab.com:43084/heroku_l3sx4prd

	public static final String DBUSER = "slassignment";
	public static final String DBPASSWORD = "streamlabs123";
	public static final String URI = "mongodb://" + DBUSER + ":" + DBPASSWORD
			+ "@ds243084.mlab.com:43084/heroku_l3sx4prd";

	public static final String DATABASE = "heroku_l3sx4prd";
	public static final String COLLECTION_USERS = "users";
	public static final String COLLECTION_CHATS = "chats";

	private static MongoClient mc = new MongoClient(new MongoClientURI(URI));
	private static MongoDatabase md = mc.getDatabase(DATABASE);

	public static MongoDatabase getDatabase() {
		return md;
	}
}
