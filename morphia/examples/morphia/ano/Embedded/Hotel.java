package morphia.ano.Embedded;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

@Entity
public class Hotel {

    @Id
    private String id;

    private String name;
    private int stars;

    @Embedded
    private Address address;
//    @Embedded("blog_comments")
//    private List<Comment> comments;
    // ... getters and setters
}

