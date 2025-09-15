package com.webserver.evrentalsystem.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatter {
    public static String formatTimestampToDateString(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateTime.format(formatter);
    }

    public static String formatTimestampToDateTimeString(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
        return dateTime.format(formatter);
    }

    public static String formatTimestampToDateTimeString(long timestamp, String pattern) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static String formatCurrency(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(amount);
    }

    public static String formatMinutesToHourMinuteString(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        // Format the hour and minute parts with leading zeros if necessary
        String formattedHours = String.format("%02d", hours);
        String formattedMinutes = String.format("%02d", remainingMinutes);

        return formattedHours + ":" + formattedMinutes;
    }
}
