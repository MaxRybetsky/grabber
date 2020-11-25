package ru.job4j.converters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts data from different sites
 * to common view.
 */
public class Converter {
    /**
     * Months map. Keeps info about orders of months
     * and Russian moths names.
     */
    private static final Map<String, Integer> MONTHS = new HashMap<>();

    static {
        initMonthsMap();
    }

    /**
     * Convert data format from sql.ru (in {@link String} type)
     * to {@link LocalDateTime}.
     *
     * @param date String of date, e.g. "22 ноя 20, 22:12" or
     *             "вчера, 22:16".
     * @return {@link LocalDateTime} object represents input string date in
     * right format for working in code.
     */
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
        int month = MONTHS.get(data[1]);
        int year = 2000 + Integer.parseInt(data[2].substring(0, 2));
        return LocalDate.of(year, month, day)
                .atTime(LocalTime.parse(data[3]));
    }

    /**
     * Initializes map {@link Converter#MONTHS} where
     * key is Russian name of month and value is number
     * of this month.
     */
    private static void initMonthsMap() {
        MONTHS.put("янв", 1);
        MONTHS.put("фев", 2);
        MONTHS.put("мар", 3);
        MONTHS.put("апр", 4);
        MONTHS.put("май", 5);
        MONTHS.put("июн", 6);
        MONTHS.put("июл", 7);
        MONTHS.put("авг", 8);
        MONTHS.put("сен", 9);
        MONTHS.put("окт", 10);
        MONTHS.put("ноя", 11);
        MONTHS.put("дек", 12);
    }
}
