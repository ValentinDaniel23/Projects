package Lab3.task1;

public class CandyBox {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandyBox candybox = (CandyBox) o;
        return flavor.equals(candybox.flavor) && origin.equals(candybox.origin);
    }

    private String flavor;
    private String origin;

    public CandyBox() {
        this.flavor = "sugar-free";
        this.origin = "Switzerland";
    }

    public CandyBox(String flavor, String origin) {
        this.flavor = flavor;
        this.origin = origin;
    }
    public float getVolume() {
        return 0;
    }

    @Override
    public String toString() { return "The " + flavor + " " + origin + " chocolate"; }
}
