package ru.job4j.converters;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ConverterTest {
    @Test
    public void whenDateInHumanFormat() {
        assertThat(Converter.stringToLocalDateTime("21 ноя 20, 22:41"),
                is(LocalDateTime.of(2020, 11, 21, 22, 41)));
    }

    @Test
    public void whenTodayDateInHumanFormat() {
        assertThat(Converter.stringToLocalDateTime("сегодня, 10:53"),
                is(LocalDate.now()
                        .atTime(10, 53)));
    }

    @Test
    public void whenYesterdayDateInHumanFormat() {
        assertThat(Converter.stringToLocalDateTime("вчера, 22:02"),
                is(LocalDate.now()
                        .minusDays(1)
                        .atTime(22, 2)));
    }
}