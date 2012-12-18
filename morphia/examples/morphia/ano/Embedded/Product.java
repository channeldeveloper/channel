package morphia.ano.Embedded;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.emul.org.bson.types.ObjectId;
import com.google.code.morphia.utils.IndexDirection;

@Entity
public class Product {

	@Id
	private ObjectId id;

	// value: Indicates the direction of the index; IndexDirection.ASC
	// (ascending), IndexDirection.DESC (descending), IndexDirection.BOTH
	// (both), default is ascending
	@Indexed(value = IndexDirection.ASC, name = "upc", unique = true, dropDups = true)
	private String upcSymbol;
	// ...
}