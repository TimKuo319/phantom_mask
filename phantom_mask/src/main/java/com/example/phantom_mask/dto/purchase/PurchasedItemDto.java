package com.example.phantom_mask.dto.purchase;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchasedItemDto {
    private int maskId;
    private String maskName;
    private String pharmacyName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
