package Lab5.task4;

public class Song {
    private String name, composer;
    private int id;

    public Song(String name, int id, String composer) {
        this.name = name;
        this.id = id;
        this.composer = composer;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getComposer() {
        return composer;
    }
    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String toString() {
        return "Song{name=" + name + ", id=" + id + ", composer=" + composer + "}";
    }
}
