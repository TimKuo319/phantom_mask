package com.example.phantom_mask.dao.purchase;

import com.example.phantom_mask.dto.purchase.MaskInfoDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PurchaseDao {
    BigDecimal getUserBalance(int userId);
    MaskInfoDto getMaskInfo(int maskId);
    void updateUserBalance(int userId, BigDecimal newBalance);
    void updatePharmacyBalance(int storeId, BigDecimal additionalAmount);
    void createTransaction(int userId, int storeId, int maskId, BigDecimal transactionAmount, LocalDateTime transactionDate);
}
