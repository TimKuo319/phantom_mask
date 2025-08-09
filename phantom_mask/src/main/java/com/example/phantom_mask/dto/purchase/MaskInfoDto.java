package com.example.phantom_mask.dto.purchase;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaskInfoDto {
    private int maskId;
    private String maskName;
    private BigDecimal price;
    private int storeId;
    private String storeName;

    private int purchaseQuantity;
    private BigDecimal totalPrice;
}
