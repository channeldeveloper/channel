package morphia.ano;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.emul.org.bson.types.ObjectId;
import com.google.code.morphia.utils.IndexDirection;
import com.mongodb.Mongo;
//db
//collection
//document
@Entity
public class MyEntity {
	@Id
	ObjectId id;
	String name;
	
	public static void main(String[] as) throws Exception
	{
		  Mongo mongo = new Mongo("localhost");
		  
	        mongo.dropDatabase("channel");
	        Morphia morphia = new Morphia();

	        morphia.mapPackage("demo.gettingStarted.MyEntity");//mapping class to db collection.
	        
	        Datastore datastore = morphia.createDatastore(mongo, "channel");//create db
	        
	        datastore.ensureCaps();// //creates capped collections from @Entity
	        datastore.ensureIndexes();// //creates indexes from @Index annotations in your entities
	        
	        //get a collection's document.
	        MyEntity entity = datastore.get(MyEntity.class, "id");

	}
	
}

