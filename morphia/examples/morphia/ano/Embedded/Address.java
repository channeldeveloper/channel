package morphia.ano.Embedded;

import com.google.code.morphia.annotations.Embedded;


//You can also choose to create a class that will be embedded 
//in the Entity, we use the @Embedded annotation in this case.
//For example, the Hotel class above might have an Address. 
//The Address would be an inseparable part of the Hotel, would not have its own ID, and would not be stored in a separate collection. In this case we would annotate the Address class as @Embedded:
	
@Embedded
public class Address {

    private String street;
    private String city;
    private String postCode;
    private String country;

    // ... getters and setters
}