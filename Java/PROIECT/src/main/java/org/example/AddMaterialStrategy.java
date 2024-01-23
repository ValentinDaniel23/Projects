package org.example;

public class AddMaterialStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(boolean val) {
        int add = 100;
        if (val == true) return add;
        else return -add;
    }
}
