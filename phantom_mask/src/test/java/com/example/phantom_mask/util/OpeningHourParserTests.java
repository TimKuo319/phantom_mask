package com.example.phantom_mask.util;

import com.example.phantom_mask.model.OpeningHour;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpeningHourParserTests {
    @Test
    void testSingleDay() {
        String raw = "Mon 08:00 - 17:00";
        List<OpeningHour> result = OpeningHourParser.parse(raw);
        assertEquals(1, result.size());
        OpeningHour oh = result.get(0);
        assertEquals("Mon", oh.getDayOfWeek());
        assertEquals(LocalTime.of(8, 0), oh.getStartTime());
        assertEquals(LocalTime.of(17, 0), oh.getEndTime());
        assertFalse(oh.isCrossNight());
    }

    @Test
    void testDayRange() {
        String raw = "Mon - Wed 09:00 - 18:00";
        List<OpeningHour> result = OpeningHourParser.parse(raw);
        assertEquals(3, result.size());
        assertEquals("Mon", result.get(0).getDayOfWeek());
        assertEquals("Tue", result.get(1).getDayOfWeek());
        assertEquals("Wed", result.get(2).getDayOfWeek());
    }

    @Test
    void testMultipleSegments() {
        String raw = "Mon - Fri 08:00 - 17:00 / Sat, Sun 08:00 - 12:00";
        List<OpeningHour> result = OpeningHourParser.parse(raw);
        assertEquals(7, result.size());
        assertEquals("Mon", result.get(0).getDayOfWeek());
        assertEquals("Sat", result.get(5).getDayOfWeek());
        assertEquals(LocalTime.of(8, 0), result.get(5).getStartTime());
        assertEquals(LocalTime.of(12, 0), result.get(5).getEndTime());
    }

    @Test
    void testCrossNight() {
        String raw = "Fri 22:00 - 02:00";
        List<OpeningHour> result = OpeningHourParser.parse(raw);
        assertEquals(1, result.size());
        OpeningHour oh = result.get(0);
        assertTrue(oh.isCrossNight());
        assertEquals(LocalTime.of(22, 0), oh.getStartTime());
        assertEquals(LocalTime.of(2, 0), oh.getEndTime());
    }

    @Test
    void testEmptyOrNull() {
        assertTrue(OpeningHourParser.parse("").isEmpty());
        assertTrue(OpeningHourParser.parse(null).isEmpty());
    }
}

