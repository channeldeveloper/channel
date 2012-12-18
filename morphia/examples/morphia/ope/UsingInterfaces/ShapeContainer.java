package morphia.ope.UsingInterfaces;

import java.util.List;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.testmodel.ShapeShifter;

public class ShapeContainer {
    private List<Shape> shapes;

	// ...o

	public void use() {
		// Now we want to store the Shape Container in Mongo. The only problem
		// for Morphia when dealing with interfaces is knowing which
		// implementation class to instantiate. That is, when retrieving the
		// first shape from Mongo, how does Morphia know whether to map it to a
		// Rectangle or a Circle?
		//
		// Morphia solves this by storing a value in the Mongo document, called
		// "className", which holds the full class name of the Java object being
		// stored in the document.
		//
		// We just need to make sure that all the implementation classes are
		// added to the Morphia instance:

		Morphia morphia = new Morphia();
		morphia.map(Circle.class).map(Rectangle.class).map(ShapeShifter.class);
	}
}