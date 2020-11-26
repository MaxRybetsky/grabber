package grabber;

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
     * @return {@code true} if post was successfully
     * saved, otherwise - {@code false}.
     */
    boolean save(Post post);

    /**
     * Retrieves from storage all early saved
     * posts.
     *
     * @return List of posts with job's info.
     */
    List<Post> getAll();

    /**
     * Searches and returns post from the
     * storage with the specifying id.
     *
     * @param id ID of post.
     * @return {@link Post}'s object.
     */
    Post findById(int id);
}
