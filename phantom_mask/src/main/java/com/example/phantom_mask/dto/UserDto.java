package com.example.phantom_mask.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {
    private int id;
    private String name;
    private BigDecimal cashBalance;
    private BigDecimal totalSpent;
}
