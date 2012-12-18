package morphia.ope.UsingInterfaces;
public class Rectangle implements Shape {
    private double height;
    private double width;

    public Rectangle() {}

    public Rectangle(double height, double width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public double getArea() {
        return height * width;
    }
}