package morphia.ano.Indexed;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.emul.org.bson.types.ObjectId;
import com.google.code.morphia.utils.IndexDirection;

/*
 * This annotation applies an index to a field. The indexes are applied when the 
 * datastore.ensureIndexes() 
 * method is called... more on this below.

 You apply the Indexed annotation on the field
 you want indexed by MongoDB.

 @Entity
 public class Product {

 @Id
 private ObjectId id;

 @Indexed(value=IndexDirection.ASC, name="upc", unique=true, dropDups=true) 
 private String upcSymbol;
 ...
 }
 The parameters are:

 value: Indicates the direction of the index; IndexDirection.ASC (ascending), 
 IndexDirection.DESC (descending), IndexDirection.BOTH (both), default is ascending

 name: The name of the index to create; default is to let the mongodb 
 create a name (in the form of key1_1/-1_key2_1/-1...

 unique: Creates the index as a unique value index; inserting duplicates values in this field will cause errors, default false

 dropDups: Tells the unique index to drop duplicates silently when creating; only the first will be kept. default false

 Datastore.ensureIndexes() needs to be called to apply the indexes to MongoDB.
  The method should be called after you have registered your entities with Morphia. 
  It will then synchronously create your indexes. 
  This should probably be done each time you start your application.

 Note: Doing this on an existing system, with existing indexes and capped collections,
  should take no time (and do nothing).

 Morphia m = ...
 Datastore ds = ...
 m.map(Product.class);
 ds.ensureIndexes(); //creates all defined with @Indexed
 You can read more about MongoDB indexes 
 here, http://www.mongodb.org/display/DOCS/Indexes
 */
@Entity
public class Product {

    @Id
    private ObjectId id;

    @Indexed(value=IndexDirection.ASC, name="upc", unique=true, dropDups=true) 
    private String upcSymbol;
//...
}