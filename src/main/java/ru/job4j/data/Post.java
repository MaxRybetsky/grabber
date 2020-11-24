package ru.job4j.data;

import java.time.LocalDateTime;

public class Post {
    private final String title;
    private final String description;
    private final LocalDateTime date;
    private final String link;

    public Post(String title, String description, LocalDateTime date, String link) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Post{"
                + "title='" + title + '\''
                + ", description='" + description + '\''
                + ", date=" + date
                + ", link='" + link + '\''
                + '}';
    }
}
