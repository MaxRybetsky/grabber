package ru.job4j.grabber;

import java.util.List;

/**
 * Simple parser to search and get data from a site.
 */
public interface Parse {
    /**
     * Iterates over all links at page
     * with the specify link and sends
     * every link of this set of links
     * to {@link Parse#detail} for extract
     * data of every post. The adds this
     * post to list and returns list.
     *
     * @param link Link to page with list of links to posts.
     * @return List of posts.
     */
    List<Post> list(String link);

    /**
     * Extracts data of post: gets
     * its title, description, link
     * and date and returns {@link Post}
     * object with these parameters.
     *
     * @param link Link to page
     *             with job info.
     * @return New {@link Post} object with
     * jobs info.
     */
    Post detail(String link);
}
