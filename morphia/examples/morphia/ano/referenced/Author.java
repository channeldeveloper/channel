package morphia.ano.referenced;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

@Entity
public class Author {

    @Id
    private ObjectId id;

    private String username;
    private String fullName;
    private String emailAddress;

    // getters and setters
}