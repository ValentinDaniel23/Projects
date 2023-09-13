package Lab3.task2;

import Lab3.task1.CandyBox;

public class Lindt extends CandyBox {
    private float length;
    private float width;
    private float height;

    public Lindt() {
        this.length = 0.0f;
        this.width = 0.0f;
        this.height = 0.0f;
    }

    public Lindt(String flavor, String origin, float length, float width, float height) {
        super(flavor, origin);
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public float getVolume() {
        return length * width * height;
    }

    public void printLindtDim() {
        System.out.println("Lindt Dimensions: Length=" + length + ", Width=" + width + ", Height=" + height);
    }

    @Override
    public String toString() {
        return "Lindt: " + super.toString() + " has volume " + getVolume();
    }
}
