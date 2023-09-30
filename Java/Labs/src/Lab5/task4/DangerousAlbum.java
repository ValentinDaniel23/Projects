package Lab5.task4;

public class DangerousAlbum extends Album {
    public void addSong(Song song) {
        if(isPrime(song.getId())) {
           songs.add(song);
        }
    }

    private boolean isPrime(int id) {
        for(int i=2; i*i<=id; i++) {
            if(id%i == 0) return false;
        }
        return true;
    }
}
