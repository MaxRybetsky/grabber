package ru.job4j.grabber;

import java.util.List;

/**
 * Store of parsed data.
 */
public interface Store {
    /**
     * Saves the {@link Post}'s object
     * to storage.
     *
     * @param post {@link Post}'s object
     *             with job's info to save.
     */
    void save(Post post);

    /**
     * Retrieves from storage all early saved
     * posts.
     *
     * @return List of posts with job's info.
     */
    List<Post> getAll();
}
