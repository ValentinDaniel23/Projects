package org.example;

public class AddReviewStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(boolean val) {
        int add = 5;
        if (val == true) return add;
        else return -add;
    }
}
