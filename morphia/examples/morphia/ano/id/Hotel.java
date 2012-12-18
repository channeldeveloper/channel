package morphia.ano.id;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

//Classes annotated with @Entity require unique @Id values; 
//these values are stored in the MongoDB "id" field, which has
//a unique index requirement. The Hotel class above would have:
@Entity
public class Hotel {

    @Id
    private ObjectId id;
//...
}