package Lab2.task1;

public class Complex {
    private int real;
    private int imaginary;

    public Complex(int real, int imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex() {
        this.real = 0;
        this.imaginary = 0;
    }

    public Complex(Complex number) {
        this.real = number.real;
        this.imaginary = number.imaginary;
    }

    public void setReal(int real) {
        this.real = real;
    }

    public void setImaginary(int imaginary) {
        this.imaginary = imaginary;
    }

    public int getReal() {
        return this.real;
    }

    public int getImaginary() {
        return this.imaginary;
    }

    public void addWithComplex(Complex number) {
        this.real += number.real;
        this.imaginary += number.imaginary;
    }

    public void showNumber() {
        if(imaginary > 0) {
            System.out.println(real + " + i * " + imaginary);
            return;
        }

        if(imaginary < 0) {
            System.out.println(real + " - i * " + (-imaginary));
            return;
        }

        System.out.println(real);
    }
}
