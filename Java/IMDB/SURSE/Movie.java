package org.example;

public class Movie extends Production {
    private String duration;
    private String releaseYear;

    public Movie(String title, String description, Double averageRating, String duration, String releaseYear) {
        super(title, description, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    void displayInfo()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Movie : " + getTitle() + '\n');
        stringBuilder.append("Directors : " + getDirectorsName() + '\n');
        stringBuilder.append("Actors : " + getActorsName() + '\n');
        stringBuilder.append("Genre : " + getGenres() + '\n');
        stringBuilder.append("Plot : " + getDescription() + '\n');
        stringBuilder.append("Average Rating : " + getAverageRating() + '\n');
        stringBuilder.append("Duration : " + getDuration() + '\n');
        stringBuilder.append("Release Year : " + getReleaseYear() + '\n');
        System.out.println(stringBuilder);
    }
}
