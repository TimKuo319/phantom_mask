package com.example.phantom_mask.etl.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class OpeningHour {
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isCrossNight;
}
