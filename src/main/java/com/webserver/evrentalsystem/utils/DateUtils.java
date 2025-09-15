package com.webserver.evrentalsystem.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    public static long getStartOfDay(long timestamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.toLocalDate().atStartOfDay();
        return startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long getEndOfDay(long timestamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.toLocalDate().atTime(23, 59, 59);
        return endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
