package fr.cmp.kyrios.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH'h'mm");

    private DateTimeUtil() {
    }

    public static String formatDisplayDateTime(LocalDateTime value) {
        return value != null ? value.format(DISPLAY_FORMAT) : "";
    }
}
