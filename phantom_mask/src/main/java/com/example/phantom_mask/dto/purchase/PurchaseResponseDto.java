package com.example.phantom_mask.dto.purchase;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseResponseDto {
    private String message;
    private BigDecimal totalAmount;
    private BigDecimal remainingBalance;
    private LocalDateTime purchaseDateTime;
    private List<PurchasedItemDto> purchasedItems;
}
