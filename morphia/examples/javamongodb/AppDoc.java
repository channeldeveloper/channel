package javamongodb;
 
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
 
/**
 * Java MongoDB : Delete document
 * 
 */
 
public class AppDoc {
	public static void main(String[] args) {
 
		try {
 
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("song");
 
			// get a single collection
			DBCollection collection = db.getCollection("users1");
 
			//insert number 1 to 10 for testing
			for (int i=1; i <= 10; i++) {
				collection.insert(new BasicDBObject().append("phonenumber", i));
			}
 
			//remove number = 1
			DBObject doc = collection.findOne(); //get first document
			collection.remove(doc);
 
			//remove number = 2
			BasicDBObject document = new BasicDBObject();
			document.put("phonenumber", 2);
			collection.remove(document);
 
			//remove number = 3
			collection.remove(new BasicDBObject().append("phonenumber", 3));
 
			//remove number > 9 , means delete number = 10
			BasicDBObject query = new BasicDBObject();
			query.put("phonenumber", new BasicDBObject("$gt", 9));
			collection.remove(query);
 
			//remove number = 4 and 5
			BasicDBObject query2 = new BasicDBObject();
			List<Integer> list = new ArrayList<Integer>();
			list.add(4);
			list.add(5);
			query2.put("phonenumber", new BasicDBObject("$in", list));
			collection.remove(query2);
 
			//remove all documents
			//DBCursor cursor = collection.find();
			//while (cursor.hasNext()) {
			//	collection.remove(cursor.next());
			//}
 
			//remove all documents , no query means delete all
			//collection.remove(new BasicDBObject());
 
			//print out the document
			DBCursor cursor = collection.find();
	                while(cursor.hasNext()) {
	                     System.out.println(cursor.next());
	                }
 
	                System.out.println("Done");
 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
 
	}
}