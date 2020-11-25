package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * PostgreSQL database storage.
 */
public class PsqlStore implements Store, AutoCloseable {
    /**
     * Connection to database.
     */
    private final Connection cnn;

    /**
     * Initializes new postgres store with
     * specifying properties.
     *
     * @param cfg Connection properties.
     */
    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.login"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Saves post's object and sets its id in the storage
     * table. If there are object with the same link value
     * saving operation will be end unsuccessfully.
     *
     * @param post {@link Post}'s object
     *             with job's info to save.
     * @return {@code true} if post was successfully
     * saved, otherwise - {@code false}.
     */
    @Override
    public boolean save(Post post) {
        String query = "insert into post(name, post_desc, link, created)"
                + "values(?,?,?,?);";
        try (PreparedStatement statement = cnn.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getDate()));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    post.setId(keys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            String duplicateErrorCode = "23505";
            if (!duplicateErrorCode.equals(e.getSQLState())) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    /**
     * Retrieves from storage all early saved
     * posts.
     *
     * @return List of posts with job's info.
     */
    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        String query = "select * from post;";
        try (PreparedStatement statement = cnn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String desc = resultSet.getString(3);
                String link = resultSet.getString(4);
                LocalDateTime date = resultSet.getTimestamp(5)
                        .toLocalDateTime();
                result.add(new Post(id, title, desc, date, link));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    /**
     * Searches and returns post from the
     * storage with the specifying id.
     *
     * @param id ID of post
     * @return {@link Post}'s object
     */
    @Override
    public Post findById(int id) {
        Post post = null;
        String query = "select * from post where id=?;";
        try (PreparedStatement statement = cnn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString(2);
                String desc = resultSet.getString(3);
                String link = resultSet.getString(4);
                LocalDateTime date = resultSet.getTimestamp(5)
                        .toLocalDateTime();
                post = new Post(id, title, desc, date, link);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return post;
    }

    /**
     * Closes this resource
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        Post post1 = new Post("post1", "post desc 1", LocalDateTime.now(), "link1");
        Post post2 = new Post("post2", "post desc 2", LocalDateTime.now(), "link2");
        Post post3 = new Post("post3", "post desc 3", LocalDateTime.now(), "link1");
        Post post4 = new Post("post4", "post desc 4", LocalDateTime.now(), "link3");
        List<Post> posts = List.of(post1, post2, post3, post4);
        Properties cnf = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            cnf.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PsqlStore psqlStore = new PsqlStore(cnf);
        for (Post post : posts) {
            if (!psqlStore.save(post)) {
                System.out.println("Can't save ");
                System.out.println(post);
            } else {
                System.out.println("Post was saved!");
            }
        }
        List<Post> postsFromDB = psqlStore.getAll();
        for (Post post : postsFromDB) {
            System.out.println(post);
        }
        System.out.println("Trying to find the next post: ");
        System.out.println(post2);
        Post goalPost = psqlStore.findById(post2.getId());
        if (goalPost == null) {
            System.out.println("Can't find this post! "
                    + "Probably, there are post with the same "
                    + "link value in the database");
        } else {
            System.out.println("Post was found!");
            System.out.println(goalPost);
        }

    }
}