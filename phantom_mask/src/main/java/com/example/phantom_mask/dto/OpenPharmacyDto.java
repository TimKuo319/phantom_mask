package com.example.phantom_mask.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class OpenPharmacyDto {
    private Long id;
    private String name;
    private String todayHours;
    private String closesIn;

    @JsonIgnore
    private LocalTime startTime;

    @JsonIgnore
    private LocalTime endTime;

    @JsonIgnore
    private boolean crossNight;
}