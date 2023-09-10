package Lab2.task3;

class Polygon {
    private Point[] points;

    public Polygon(int n) {
        points = new Point[n];
    }

    public Polygon(float[] coordinates) {
        if (coordinates.length % 2 != 0) {
            throw new IllegalArgumentException("Numărul de coordonate trebuie să fie par.");
        }

        int n = coordinates.length / 2;
        points = new Point[n];

        for (int i = 0, j = 0; i < n; i++, j += 2) {
            float x = coordinates[j];
            float y = coordinates[j + 1];
            points[i] = new Point(x, y);
        }
    }

    public void display() {
        for (int i = 0; i < points.length; i++) {
            System.out.println("Punct " + (i + 1) + ": " + points[i].toString());
        }
    }
}
