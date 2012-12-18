package morphia.ano.noclass;

import com.google.code.morphia.annotations.Entity;

////The default behavior is to store the class name in the document.
//
//Why would you need it? This is mainly used when storing different entities in the same collection and reading them back as the base or super class.
//
//Ex.
//
//@Entity("animals") abstract class Animal { String name; } 
//@Entity("animals") Cat extends Animal { ... } 
//@Entity("animals") Dog extends Animal { ... } 
//
////And then performing the following query...
//List<Animal> animals = ds.createQuery(Animal.class).asList(); 
//As you can see, without the class name stored in the document, Morphia wouldn't know which class to actually create.
//
//If you are only storing a single entity type in the collection and you are concerned about datasize, it would be safe to add the noClassnameStored=true parameter to the Entity annotation.
@Entity(value="hotels", noClassnameStored=true)
public class Hotel {

    public Hotel() {
    }

}