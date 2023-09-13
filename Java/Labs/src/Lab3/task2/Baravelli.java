package Lab3.task2;

import java.lang.Math;
import Lab3.task1.CandyBox;

public class Baravelli extends CandyBox {
    private float radius;
    private float height;

    public Baravelli() {
        this.radius = 0.0f;
        this.height = 0.0f;
    }

    public Baravelli(String flavor, String origin, float radius, float height) {
        super(flavor, origin);
        this.radius = radius;
        this.height = height;
    }

    public float getVolume() {
        return (float) Math.PI * (float) Math.pow(radius, 2.0f) * height;
    }

    public void printBaravelliDim() {
        System.out.println("Baravelli Dimensions: Radius=" + radius + ", Height=" + height);
    }

    @Override
    public String toString() {
        return "Baravelli: " + super.toString() + " has volume " + getVolume();
    }
}
