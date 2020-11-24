package ru.job4j.data;

import java.time.LocalDateTime;

public class Post {
    private final String title;
    private final String description;
    private final LocalDateTime date;

    public Post(String title, String description, LocalDateTime date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
