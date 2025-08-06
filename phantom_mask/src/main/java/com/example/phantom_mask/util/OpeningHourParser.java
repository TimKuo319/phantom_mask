package com.example.phantom_mask.util;


import com.example.phantom_mask.etl.model.OpeningHour;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

public class OpeningHourParser {

    private static final List<String> DAYS = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static List<OpeningHour> parse(String rawHours) {
        List<OpeningHour> results = new ArrayList<>();

        if (rawHours == null || rawHours.isBlank()) return results;

        // Split into multiple time segments, e.g. "Mon - Fri 08:00 - 17:00 / Sat, Sun 08:00 - 12:00"
        String[] segments = rawHours.split("\\s*/\\s*");

        for (String segment : segments) {
            // Use regex to extract "Mon - Fri" and "08:00 - 17:00"
            Pattern pattern = Pattern.compile("(?<days>[A-Za-z,\\- ]+)\\s+(?<start>\\d{2}:\\d{2})\\s*-\\s*(?<end>\\d{2}:\\d{2})");
            Matcher matcher = pattern.matcher(segment.trim());

            if (matcher.find()) {
                String daysPart = matcher.group("days").trim();
                LocalTime start = LocalTime.parse(matcher.group("start"), TIME_FORMATTER);
                LocalTime end = LocalTime.parse(matcher.group("end"), TIME_FORMATTER);
                boolean isCrossNight = start.isAfter(end);

                for (String day : expandDays(daysPart)) {
                    results.add(new OpeningHour(day, start, end, isCrossNight));
                }
            }
        }

        return results;
    }

    // Handle formats like "Mon - Wed", "Tue, Thu", "Fri"
    private static List<String> expandDays(String daysPart) {
        List<String> result = new ArrayList<>();

        for (String token : daysPart.split(",")) {
            token = token.trim();

            if (token.contains("-")) {
                String[] range = token.split("-");
                if (range.length == 2) {
                    String start = range[0].trim();
                    String end = range[1].trim();
                    int startIdx = DAYS.indexOf(start);
                    int endIdx = DAYS.indexOf(end);

                    if (startIdx != -1 && endIdx != -1 && startIdx <= endIdx) {
                        result.addAll(DAYS.subList(startIdx, endIdx + 1));
                    }
                }
            } else {
                if (DAYS.contains(token)) {
                    result.add(token);
                }
            }
        }

        return result;
    }
}
