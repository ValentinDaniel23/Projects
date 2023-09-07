package Lab1.task3;

public class Main {
    public static void main(String[] args) {
        Student firstChild = new Student("Mihai", 7.81);
        Student secondChild = firstChild;

        if (firstChild.equals(secondChild)) {
            System.out.println("Equal objects");
        }
        else
            System.out.println("Different objects");
    }
}
