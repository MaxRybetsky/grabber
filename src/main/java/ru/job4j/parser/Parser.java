package ru.job4j.parser;

import ru.job4j.data.Post;

import java.util.List;

public interface Parser {
    List<Post> list(String link);

    Post detail(String link);
}
