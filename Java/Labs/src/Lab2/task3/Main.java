package Lab2.task3;

public class Main {
    public static void main(String[] args) {
        // Exemplu folosind constructorul cu un număr de colțuri
        Polygon polygon1 = new Polygon(4);
        // polygon1.display();

        // Exemplu folosind constructorul cu un vector de coordonate
        float[] coordinates = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
        Polygon polygon2 = new Polygon(coordinates);
        polygon2.display();
    }
}
