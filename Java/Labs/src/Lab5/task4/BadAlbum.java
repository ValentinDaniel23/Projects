package Lab5.task4;

public class BadAlbum extends Album {
    public void addSong(Song song) {
        if (song.getName().length() == 3 && isPalindrome(song.getId())) {
            songs.add(song);
        }
    }

    private boolean isPalindrome(int number) {
        int originalNumber = number;
        int reversedNumber = 0;
        while (number != 0) {
            int remainder = number % 10;
            reversedNumber = reversedNumber * 10 + remainder;
            number /= 10;
        }
        return originalNumber == reversedNumber;
    }
}
