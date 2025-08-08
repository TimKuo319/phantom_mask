package com.example.phantom_mask.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class MaskTransactionStatsDto {
    private int totalMaskCount;
    private BigDecimal totTransactionAmount;
}
