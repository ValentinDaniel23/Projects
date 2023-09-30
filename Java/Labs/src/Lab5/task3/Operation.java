package Lab5.task3;

public class Operation implements Minus, Plus, Mult, Div {
    private float value;

    public Operation(float value) {
        this.value = value;
    }

    @Override
    public void minus(float valueToSubtract) {
        value -= valueToSubtract;
    }

    @Override
    public void plus(float valueToAdd) {
        value += valueToAdd;
    }

    @Override
    public void mult(float valueToMultiplyBy) {
        value *= valueToMultiplyBy;
    }

    @Override
    public void div(float valueToDivideBy) {
        if (valueToDivideBy != 0) {
            value /= valueToDivideBy;
        } else {
            System.out.println("Division by zero is not possible");
        }
    }

    public float getNumber() {
        return value;
    }
}
