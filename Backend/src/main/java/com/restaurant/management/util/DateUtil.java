package com.restaurant.management.util;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
    public static LocalDateTime startOfDay(LocalDateTime dt) { return dt.toLocalDate().atStartOfDay(); }
    public static LocalDateTime endOfDay(LocalDateTime dt) { return dt.toLocalDate().atTime(23,59,59); }
    public static String format(LocalDateTime dt) { return dt != null ? dt.format(DISPLAY_FMT) : ""; }
}