package me.dolphago.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public static LocalDateTime parse(LocalDateTime date){
        return LocalDateTime.parse(date.toString(), FORMATTER);
    }

}
