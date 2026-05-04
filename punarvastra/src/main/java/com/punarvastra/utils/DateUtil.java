package com.punarvastra.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Human-readable date/time formatting for JSP.
 */
public final class DateUtil {

    private static final DateTimeFormatter DISPLAY =
            DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", java.util.Locale.UK);

    private DateUtil() {
    }

    /**
     * Formats a timestamp for display, or empty string if null.
     *
     * @param dt database timestamp mapped to LocalDateTime
     * @return formatted text
     */
    public static String formatDisplay(LocalDateTime dt) {
        if (dt == null) {
            return "";
        }
        return dt.format(DISPLAY);
    }
}
