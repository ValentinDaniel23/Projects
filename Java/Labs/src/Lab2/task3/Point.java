package Lab2.task3;

class Point {
    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void changeCoords(float newX, float newY) {
        x = newX;
        y = newY;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}