package com.example.phantom_mask.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaskDto {
    private int id;
    private String name;
    private BigDecimal price;
}
