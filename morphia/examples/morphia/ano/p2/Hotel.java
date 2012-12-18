package morphia.ano.p2;

import com.google.code.morphia.annotations.Entity;
//You can also optionally set a name for your MongoDB
// DBCollection name. 
//You will also need a (no-args) default constructor.
@Entity("hotels")
public class Hotel {

    public Hotel() {
    }

}