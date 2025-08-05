package com.example.phantom_mask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class OpeningHour {
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isCrossNight;
}
