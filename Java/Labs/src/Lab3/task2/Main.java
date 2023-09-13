package Lab3.task2;

public class Main {
    public static void main(String[] args) {
        Lindt lindt1 = new Lindt("Chocolate", "Switzerland", 2.0f, 1.0f, 0.5f);
        Lindt lindt2 = new Lindt("Chocolate", "Switzerland", 2.0f, 1.0f, 0.5f);
        Lindt lindt3 = new Lindt("Caramel", "Belgium", 2.0f, 1.0f, 0.5f);

        System.out.println("lindt1 equals lindt2: " + lindt1.equals(lindt2));
        System.out.println("lindt1 equals lindt3: " + lindt1.equals(lindt3));
    }
}
