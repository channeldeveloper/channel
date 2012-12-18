package morphia.ano.referenced;
import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.emul.org.bson.types.ObjectId;

//One very important thing to mention when working with references:
//The referenced object must have been saved in Mongo before saving the object referencing it.

//By default, Morphia uses the field name as the value name in Mongo.
//This can be overridden by specifying a name on the @Reference annotation:
//
//    @Reference("blog_authors")
//    private List<Author> authors;

//The referenced object must have been saved in Mongo before saving the object referencing it.
@Entity
public class BlogEntry {

    @Id
    private ObjectId id;

    private String title;
    private Date publishDate;
    private String body;

    @Reference
    private Author author;

    // getters and setters
}
