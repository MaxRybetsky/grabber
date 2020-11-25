package ru.job4j.grabber;

import java.time.LocalDateTime;

/**
 * Post that keeps main data of job offer.
 */
public class Post {
    /**
     * Post's ID
     */
    private int id;

    /**
     * Post's title.
     */
    private final String title;

    /**
     * Post's description.
     */
    private final String description;

    /**
     * Post's date of creation.
     */
    private final LocalDateTime date;

    /**
     * Link to post with job's info.
     */
    private final String link;

    /**
     * Creates new Post with input job's info
     * and zero id value.
     *
     * @param title       Input post's title.
     * @param description Input post's description.
     * @param date        Input post's date of creation.
     * @param link        Input post's link to job's offer.
     */
    public Post(String title, String description, LocalDateTime date, String link) {
        this(0, title, description, date, link);
    }

    /**
     * Creates new Post with input job's info
     * and with the given id value.
     *
     * @param id          Input post's id
     * @param title       Input post's title.
     * @param description Input post's description.
     * @param date        Input post's date of creation.
     * @param link        Input post's link to job's offer.
     */
    public Post(int id, String title, String description, LocalDateTime date, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.link = link;
    }

    /**
     * Shows this {@link Post}'s object's id.
     *
     * @return {@link Post#id}.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets this {@link Post}'s object's id.
     * @param id The specify post's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Shows this {@link Post}'s object's title.
     *
     * @return {@link Post#title}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Shows this {@link Post}'s object's description.
     *
     * @return {@link Post#description}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Shows this {@link Post}'s object's date
     * of creation.
     *
     * @return {@link Post#date}
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Shows this {@link Post}'s object's link
     * to job offer.
     *
     * @return {@link Post#link}.
     */
    public String getLink() {
        return link;
    }

    /**
     * Returns a string representation of the post with
     * all this {@link Post}'s object's information.
     *
     * @return A string representation of the post.
     */
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
