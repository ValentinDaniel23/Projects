package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private RequestTypes type;
    private LocalDateTime creationDate;
    private String title;
    private String description;
    private String requesterUsername;
    private String resolverUsername;

    public Request(RequestTypes type, LocalDateTime creationDate, String title, String description, String requesterUsername, String resolverUsername) {
        this.type = type;
        this.creationDate = creationDate;
        this.title = title;
        this.description = description;
        this.requesterUsername = requesterUsername;
        this.resolverUsername = resolverUsername;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRequesterUsername() {
        return requesterUsername;
    }
    public String getResolverUsername() {
        return resolverUsername;
    }

    public RequestTypes getType() {
        return type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void getFormattedCreationDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.creationDate = LocalDateTime.parse(date, formatter);
    }
}
