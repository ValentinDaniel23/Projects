package Lab5.task4;

public class ThrillerAlbum extends Album {
    public void addSong(Song song) {
        if ("Michael Jackson".equals(song.getComposer()) && song.getId() % 2 == 0) {
           songs.add(song);
        }
    }
}
