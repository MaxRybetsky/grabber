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

public class SqlRuParse implements Parser {
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
