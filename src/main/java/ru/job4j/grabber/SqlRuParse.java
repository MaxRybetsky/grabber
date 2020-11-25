package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.converters.Converter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Jobs parser of sql.ru site.
 */
public class SqlRuParse implements Parse {
    /**
     * Extracts all posts in the page at
     * site sql.ru with the specify link
     * to the list.
     * <p>
     * Iterates over all links at page
     * with the specify link and sends
     * every link of this set of links
     * to {@link Parse#detail} for extract
     * data of every post. The adds this
     * post to list and returns list.
     *
     * @param link Link to page with list
     *             of links to posts.
     * @return List of posts at sql.ru site
     * page with the specify link.
     */
    @Override
    public List<Post> list(String link) {
        List<Post> result = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link)
                    .get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                result.add(detail(href.attr("href")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Extracts data of post at
     * site sql.ru: gets its title,
     * description, link and date
     * and returns {@link Post}
     * object with these parameters.
     *
     * @param link Link to page
     *             with job info.
     * @return New {@link Post} object with
     * jobs info at site sql.ru.
     */
    @Override
    public Post detail(String link) {
        String title = "default";
        String desc = title;
        LocalDateTime date = LocalDateTime.now();
        try {
            Document doc = Jsoup.connect(link)
                    .get();
            Elements header = doc.select(".messageHeader");
            Elements body = doc.select(".msgBody");
            Elements footer = doc.select(".msgFooter");
            title = header.first().text();
            desc = body.get(1).text();
            date = Converter.stringToLocalDateTime(footer.first()
                    .text()
                    .split("\\s\\[")[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Post(title, desc, date, link);
    }
}
