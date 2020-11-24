package ru.job4j.converters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    private static Map<String, Integer> months = new HashMap<>();

    static {
        initMonthsMap();
    }

    public static LocalDateTime stringToLocalDateTime(String date) {
        String[] data = date.split("\\s");
        if (data.length < 3) {
            if ("сегодня,".equals(data[0])) {
                return LocalDate.now()
                        .atTime(LocalTime.parse(data[1]));
            } else {
                return LocalDate.now()
                        .minusDays(1)
                        .atTime(LocalTime.parse(data[1]));
            }
        }
        int day = Integer.parseInt(data[0]);
        int month = months.get(data[1]);
        int year = 2000 + Integer.parseInt(data[2].substring(0, 2));
        return LocalDate.of(year, month, day)
                .atTime(LocalTime.parse(data[3]));
    }

    private static void initMonthsMap() {
        months.put("янв", 1);
        months.put("фев", 2);
        months.put("мар", 3);
        months.put("апр", 4);
        months.put("май", 5);
        months.put("июн", 6);
        months.put("июл", 7);
        months.put("авг", 8);
        months.put("сен", 9);
        months.put("окт", 10);
        months.put("ноя", 11);
        months.put("дек", 12);
    }
}
