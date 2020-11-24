package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.converters.Converter;
import ru.job4j.data.Post;

import java.io.IOException;
import java.time.LocalDateTime;

public class SqlRuParse {
    public static void main(String[] args) throws IOException {
        printJobsList(5);
        System.out.println(getJobInfo("https://www.sql.ru/forum/1331208/"
                + "vakansiya-programmist-razrabotchik-informatica"));
    }

    public static void printJobsList(int repeats) throws IOException {
        String url = "https://www.sql.ru/forum/job-offers/";
        for (int i = 1; i <= repeats; i++) {
            Document doc = Jsoup.connect(url + i)
                    .get();
            Elements row = doc.select(".postslisttopic");
            Elements dates = doc.select(".altCol");
            int index = 1;
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(Converter.stringToLocalDateTime(
                        dates.get(index).text()
                ));
                index += 2;
            }
        }
    }

    public static Post getJobInfo(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .get();
        Elements header = doc.select(".messageHeader");
        Elements body = doc.select(".msgBody");
        Elements footer = doc.select(".msgFooter");
        String title = header.first().text();
        String desc = body.get(1).text();
        LocalDateTime date = Converter.stringToLocalDateTime(footer.first()
                .text()
                .split("\\s\\[")[0]);
        return new Post(title, desc, date, url);
    }
}
