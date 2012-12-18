package morphia.ano.Indexed;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Transient;

//As you can see, classes with @Embedded annotation do not need an @Id. 
//This is because they always be included in another class. In fact, 
//they are not allowed to have an @Id if the class is annotated with @Embedded.

@Embedded
public class Address {
	// If you want to exclude a field from being mapped to Mongo, use the
	// @Transient annotation:
	@Transient
	private int myTransientInt;
	// ...

	// By default, Morphia uses the field name as the value
	// name in Mongo. This can be overridden by using the @Property annotation,
	// and specifying a name:

	@Property("my_integer")
	private int myInt;

}