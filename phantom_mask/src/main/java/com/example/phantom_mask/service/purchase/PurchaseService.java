package com.example.phantom_mask.service.purchase;


import com.example.phantom_mask.dto.purchase.PurchaseRequestDto;
import com.example.phantom_mask.dto.purchase.PurchaseResponseDto;

public interface PurchaseService {
    PurchaseResponseDto processPurchase(PurchaseRequestDto purchaseRequestDto);
}
