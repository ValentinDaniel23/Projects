package Lab3.task2;

import Lab3.task1.CandyBox;

public class ChocAmor extends CandyBox {
    private float length;

    public ChocAmor() {
        this.length = 0.0f;
    }

    public ChocAmor(String flavor, String origin, float lenght) {
        super(flavor, origin);
        this.length = lenght;
    }

    public float getVolume() {
        return length * length * length;
    }

    public void printChocAmorDim() {
        System.out.println("ChocAmor Dimensions: Length=" + length);
    }

    @Override
    public String toString() {
        return "ChocAmor: " + super.toString() + " has volume " + getVolume();
    }
}
