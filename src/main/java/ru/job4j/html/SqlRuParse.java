package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.converters.Converter;

import java.io.IOException;

public class SqlRuParse {
    public static void main(String[] args) throws IOException {
        printDataFromSite(5);
    }

    public static void printDataFromSite(int repeats) throws IOException {
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
}
