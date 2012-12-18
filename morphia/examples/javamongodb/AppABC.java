package javamongodb;
 
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
 
/**
 * Java + MongoDB Hello world Example
 * 
 */
public class AppABC {
	public static void main(String[] args) {
 
		try {
			// connect to mongoDB, ip and port number
			Mongo mongo = new Mongo("localhost", 27017);
 
			// get database from MongoDB,
			// if database doesn't exists, mongoDB will create it automatically
			DB db = mongo.getDB("song");
 
			// Get collection from MongoDB, database named "yourDB"
			// if collection doesn't exists, mongoDB will create it automatically
			DBCollection collection = db.getCollection("users");
 
			// create a document to store key and value
			BasicDBObject document = new BasicDBObject();
			document.put("_id", 1001);
			document.put("names", "songxueliang");
 
			// save it into collection named "yourCollection"
			collection.insert(document);
 
			// search query
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_id", "1001xxx");
 
			// query it
			DBCursor cursor = collection.find(searchQuery);
 
			// loop over the cursor and display the retrieved result
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
 
			System.out.println("Done");
			
			//connect to Mongo  DB DBCollection BasicDBObject DBCursor
 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
 
	}
}