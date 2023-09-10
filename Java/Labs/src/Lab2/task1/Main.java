package Lab2.task1;

import Lab2.task2.Student;

public class Main {
    public static void main (String[] args) {
        Complex num1 = new Complex(2, 5);
        Complex num2 = new Complex();
        Complex num3 = new Complex(5, -2);
        Complex num4 = new Complex(3, -1);
        Complex num5 = new Complex(-5, 2);
        Complex num6 = new Complex(-1, -1);

        num1.showNumber();
        num2.showNumber();
        num3.showNumber();
        num4.showNumber();
        num5.showNumber();
        num6.showNumber();

        num2 = new Complex(num1);
        num2.showNumber();

        num3.addWithComplex(num4);
        num3.showNumber();
    }
}
