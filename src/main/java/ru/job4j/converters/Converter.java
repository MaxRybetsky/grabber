package ru.job4j.converters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Converter {
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
        int month = getMonth(data[1]);
        int year = 2000 + Integer.parseInt(data[2].substring(0, 2));
        return LocalDate.of(year, month, day)
                .atTime(LocalTime.parse(data[3]));
    }

    private static int getMonth(String month) {
        switch (month) {
            case "янв":
                return 1;
            case "фев":
                return 2;
            case "мар":
                return 3;
            case "апр":
                return 4;
            case "май":
                return 5;
            case "июн":
                return 6;
            case "июл":
                return 7;
            case "авг":
                return 8;
            case "сен":
                return 9;
            case "окт":
                return 10;
            case "ноя":
                return 11;
            case "дек":
                return 12;
            default:
                return -1;
        }
    }
}
