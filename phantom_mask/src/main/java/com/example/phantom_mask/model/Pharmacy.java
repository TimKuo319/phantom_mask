package com.example.phantom_mask.model;

import lombok.Data;
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
