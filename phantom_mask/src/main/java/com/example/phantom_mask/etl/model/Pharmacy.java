package com.example.phantom_mask.etl.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class Pharmacy {
    private String name;
    private BigDecimal cashBalance;
    private String openingHours;
    private List<Mask> masks;
}
