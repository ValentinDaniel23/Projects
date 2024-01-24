package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor implements Comparable {
    private String actorName;
    private List<Map.Entry<String, String>> roles;
    private String biography;
    public Actor(String actorName, String biography) {
        this.actorName = actorName;
        this.biography = biography;
        roles = new ArrayList<>();
    }
    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public List<Map.Entry<String, String>> getRoles() {
        return roles;
    }
    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography=biography;
    }

    public void setRoles(List<Map.Entry<String, String>> roles) {
        this.roles = roles;
    }
    public void addRoles(Map.Entry<String, String> entry) {
        roles.add(entry);
    }
    public void removeRoles(Map.Entry<String, String> entry) {
        roles.remove(entry);
    }
    public Map.Entry<String, String> createEntry(String name, String type) {
        Map.Entry<String, String> entry = Map.entry(name, type);
        return entry;
    }
    public void displayInfo() {
        StringBuilder result = new StringBuilder();
        result.append("Actor: ").append(actorName).append("\n");
        if (roles != null) {
            result.append("Roles:\n");
            for (Map.Entry<String, String> role : roles) {
                result.append("   - ").append(role.getKey()).append(": ").append(role.getValue()).append("\n");
            }
        }
        result.append("Biography: ").append(biography).append("\n");
        System.out.println(result.toString());
    }
    public int compareTo(Object o) {
        if (o instanceof Actor)
            return actorName.compareTo( ((Actor) o).actorName);
        else
            return actorName.compareTo( ((Production) o).getTitle() );
    }
}
