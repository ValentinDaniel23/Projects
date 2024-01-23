package org.example;

public class AddRequestStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(boolean val) {
        int add = 50;
        if (val == true) return add;
        else return -add;
    }
}