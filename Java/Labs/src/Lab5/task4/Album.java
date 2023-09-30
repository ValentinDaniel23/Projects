package Lab5.task4;

import java.util.ArrayList;
abstract class Album {
    protected ArrayList<Song> songs;

    public Album() {
        songs = new ArrayList<>();
    }

    public abstract void addSong(Song song);

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Album{songs=[");
        for (int i = 0; i < songs.size(); i++) {
            stringBuilder.append(songs.get(i).getName());
            if (i < songs.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
