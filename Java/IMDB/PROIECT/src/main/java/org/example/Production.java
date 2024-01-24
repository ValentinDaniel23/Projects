package org.example;

import java.awt.dnd.DragGestureEvent;
import java.util.*;

public abstract class Production implements Comparable {
    private String title;
    private List<String> directorsName;
    private List<String> actorsName;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String description;
    private Double averageRating;

    public Production(String title, String description, Double averageRating) {
        this.title = title;
        this.description = description;
        this.averageRating = averageRating;
        directorsName = new ArrayList<>();
        actorsName = new ArrayList<>();
        genres = new ArrayList<>();
        ratings = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getDirectorsName() {
        return directorsName;
    }
    public void setDirectorsName(List<String> directorsName) {
        this.directorsName = directorsName;
    }
    public List<String> getActorsName() {
        return actorsName;
    }
    public void setActorsName(List<String> actorsName) {
        this.actorsName = actorsName;
    }
    public List<Genre> getGenres() {
        return genres;
    }
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
    public List<Rating> getRatings() {
        return ratings;
    }
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    public void addDirector(String director) {
        directorsName.add(director);
    }
    public void addActor(String actor) {
        actorsName.add(actor);
    }
    public void addGenre(Genre genre) {
        genres.add(genre);
    }
    public void calculateAverageRating() {
        averageRating = 0.0;
        for (Rating rating : ratings) {
            averageRating += rating.getNote();
        }

        if (!ratings.isEmpty()) {
            averageRating /= ratings.size();
            averageRating = Math.round(averageRating * 100.0) / 100.0;
        }
    }
    public boolean addRating(Rating rating) {
        for (Rating rating1 : ratings) {
            if (rating1.getUsername().equals(rating.getUsername()))
                return false;
        }
        if (IMDB.notificationIsOn) {
            for (Rating rating1 : ratings) {
                User user=IMDB.findUser(rating1.getUsername());
                if (this instanceof Movie)
                    user.update("Filmul \"" + this.title + "\" a primit un review nou de la utilizatorul " + rating.getUsername() + " -> " + String.valueOf(rating.getNote()));
                else
                    user.update("Serialul \"" + this.title + "\" a primit un review nou de la utilizatorul " + rating.getUsername() + " -> " + String.valueOf(rating.getNote()));
            }
        }
        User user = IMDB.getProductionUser(this);
        if (user != null) {
            if (this instanceof Movie) user.update("Filmul \"" + this.title + "\" pe care l-ai adaugat a primit un review nou de la " + rating.getUsername() + " -> " + String.valueOf(rating.getNote()));
            else user.update("Serialul \"" + this.title + "\" pe care l-ai adaugat a primit un review nou de la " + rating.getUsername() + " -> " + String.valueOf(rating.getNote()));
        }
        User user1 = IMDB.findUser(rating.getUsername());
        if (user1 != null) {
            user1.experienceStrategy = new AddReviewStrategy();
            user1.updateExperience(user1.experienceStrategy.calculateExperience(true));
        }

        ratings.add(rating);
        calculateAverageRating();
        return true;
    }
    public boolean removeRating(String username) {
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username)) {
                ratings.remove(rating);
                calculateAverageRating();

                if (IMDB.notificationIsOn)
                    for (Rating rating1 : ratings) {
                        User user = IMDB.findUser(rating1.getUsername());
                        if (this instanceof Movie) user.update("Filmul \"" + this.title + "\" a pierdut un review");
                        else user.update("Serialul \"" + this.title + "\" a pierdut un review");
                    }

                User user = IMDB.getProductionUser(this);
                if (user != null) {
                    if (this instanceof Movie) user.update("Filmul \"" + this.title + "\" pe care l-ai adaugat a pierdut un review");
                    else user.update("Serialul \"" + this.title + "\" pe care l-ai adaugat a pierdut un review");
                }
                User user1 = IMDB.findUser(rating.getUsername());
                if (user1 != null) {
                    user1.experienceStrategy = new AddReviewStrategy();
                    user1.updateExperience(user1.experienceStrategy.calculateExperience(false));
                }

                return true;
            }
        }
        return false;
    }
    abstract void displayInfo();

    public int compareTo(Object o) {
         if (o instanceof Production)
             return title.compareTo( ((Production) o).getTitle());
         else
             return title.compareTo( ((Actor) o).getActorName() );
    }
}
