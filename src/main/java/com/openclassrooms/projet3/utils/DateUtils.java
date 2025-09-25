package com.openclassrooms.projet3.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire pour la manipulation et le formatage des dates.
 *
 * Fournit des méthodes statiques pour convertir des objets Timestamp en chaînes
 * formatées selon un format standard (ici "yyyy/MM/dd").
 */
public class DateUtils {

    /** Formatter pour le format "yyyy/MM/dd" */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * Convertit un Timestamp en une date formatée "yyyy/MM/dd".
     *
     * @param timestamp le Timestamp à convertir (peut être null)
     * @return la date formatée sous forme de chaîne, ou null si le timestamp est null
     */
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return null;

        // Conversion du Timestamp en LocalDateTime
        LocalDateTime dateTime = timestamp.toLocalDateTime();

        // Formatage en chaîne "yyyy/MM/dd"
        return dateTime.format(FORMATTER);
    }
}

