package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Series extends Production {
    private int numSeasons;
    private String releaseYear;
    private Map<String, List<Episode> > franchise;

    public Series(String title, String description, Double averageRating, int numSeasons, String releaseYear) {
        super(title, description, averageRating);
        this.numSeasons = numSeasons;
        this.releaseYear = releaseYear;
        franchise = new HashMap<>();
    }

    public int getNumSeasons() {
        return numSeasons;
    }
    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }
    public String getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }
    public Map<String, List<Episode>> getFranchise() {
        return franchise;
    }
    public void setFranchise(Map<String, List<Episode>> franchise) {
        this.franchise = franchise;
    }

    public List<Episode> getEpisodesList(String season) {
        return franchise.get(season);
    }
    public void addEpisode(String season, Episode episode) {
        List<Episode> episodesList = this.getEpisodesList(season);
        episodesList.add(episode);
    }
    public void addSeason(String season) {
        franchise.put(season, new ArrayList<>());
    }
    void displayInfo()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Serial : " + getTitle() + '\n');
        stringBuilder.append("Directors : " + getDirectorsName() + '\n');
        stringBuilder.append("Actors : " + getActorsName() + '\n');
        stringBuilder.append("Genre : " + getGenres() + '\n');
        stringBuilder.append("Plot : " + getDescription() + '\n');
        stringBuilder.append("Average Rating : " + getAverageRating() + '\n');
        stringBuilder.append("Number of seasons : " + numSeasons + '\n');
        stringBuilder.append("Release Year : " + getReleaseYear() + '\n');
        stringBuilder.append("Seasons : " + getFranchise() + '\n');
        System.out.println(stringBuilder);
    }
}
