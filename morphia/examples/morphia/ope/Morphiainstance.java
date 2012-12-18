package morphia.ope;

import morphia.ano.referenced.Author;
import morphia.ano.referenced.BlogEntry;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.emul.org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class Morphiainstance {
	private void inti() {
		Morphia morphia = new Morphia();
		morphia.map(BlogEntry.class);
		morphia.map(Author.class);
//		morphia.mapPackage("my.package.with.only.mongo.entities");
		
//		Morphia morphia = ...;
		Mongo mongo = null;//...;
		DB db = mongo.getDB("BlogSite");

		BlogEntry blogEntry = null;//...;// this is our annotated object

		// map the blog entry to a Mongo DBObject
		DBObject blogEntryDbObj = morphia.toDBObject(blogEntry);

		// and then save that DBObject in a Mongo collection
		db.getCollection("BlogEntries").save(blogEntryDbObj);
		
		
		//Retrieving a Java from MongoDB
		//	Now let's look at the other direction: creating a 
		//Java object from a document in the Mongo database. This is also simple. 
		//We just call the fromDBObject() method on our Morphia instance, 
		//passing in the DBObject retrieved from Mongo:

//		Morphia morphia = null;//...;
//		Mongo mongo = null;//...;
		DB d1b = mongo.getDB("BlogSite");

		String blogEntryId = null;//...; // the ID of the blog entry we want to load

		// load the DBObject from a Mongo collection
		BasicDBObject bdbo = 
				(BasicDBObject) db.getCollection("BlogEntries").findOne(new BasicDBObject("_id", new ObjectId(blogEntryId)));

		// and then map it to our BlogEntry object
		BlogEntry be = morphia.fromDBObject(BlogEntry.class, blogEntryDbObj);
		
	}
//	You can also tell Morphia to scan a package, and map all classes found in that package:
//
//		...
//		morphia.mapPackage("my.package.with.only.mongo.entities");
//		...
	/**
	 * 
	 * public interface Shape { public double getArea(); }
	 * 
	 * ...
	 * 
	 * public class Rectangle implements Shape { private double height; private
	 * double width;
	 * 
	 * public Rectangle() {}
	 * 
	 * public Rectangle(double height, double width) { this.height = height;
	 * this.width = width; }
	 * 
	 * @Override public double getArea() { return height * width; } }
	 * 
	 *           ...
	 * 
	 *           public class Circle implements Shape { ... }
	 * 
	 *           ...
	 * 
	 *           public class ShapeContainer { private List<Shape> shapes; ... }
	 */
}
