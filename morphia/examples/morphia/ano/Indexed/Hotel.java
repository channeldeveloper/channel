package morphia.ano.Indexed;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

@Entity
public class Hotel {
//    ...
    @Id
    private ObjectId id;

    @Embedded
    private Address address;

}



