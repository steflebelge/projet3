package com.openclassrooms.projet3.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    // Formatter pour le format "yyyy/MM/dd"
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * Convertit un Timestamp en une date formatée "yyyy/MM/dd"
     * @param timestamp le Timestamp à convertir
     * @return la date formatée
     */
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return null;
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return dateTime.format(FORMATTER);
    }

}
