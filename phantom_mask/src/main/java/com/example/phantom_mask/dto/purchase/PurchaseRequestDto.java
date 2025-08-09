package com.example.phantom_mask.dto.purchase;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequestDto {
    private int userId;
    private List<PurchaseItemDto> items;
}
