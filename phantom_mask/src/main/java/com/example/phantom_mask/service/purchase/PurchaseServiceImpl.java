package com.example.phantom_mask.service.purchase;

import com.example.phantom_mask.dao.purchase.PurchaseDao;
import com.example.phantom_mask.dto.purchase.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;

    public PurchaseServiceImpl(PurchaseDao purchaseDao) {
        this.purchaseDao= purchaseDao;
    }

    @Override
    @Transactional
    public PurchaseResponseDto processPurchase(PurchaseRequestDto purchaseRequest) {
        validatePurchaseRequest(purchaseRequest);

        int userId = purchaseRequest.getUserId();
        List<PurchaseItemDto> items = purchaseRequest.getItems();

        BigDecimal userBalance = purchaseDao.getUserBalance(userId);
        if(userBalance == null) {
            throw new IllegalArgumentException("User not found with ID:" + userId);
        }

        List<MaskInfoDto> maskInfos = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(PurchaseItemDto item : items) {
            MaskInfoDto maskInfo = purchaseDao.getMaskInfo(item.getMaskId());
            if (maskInfo == null) {
                throw new IllegalArgumentException("Mask not found with ID: " + item.getMaskId());
            }

            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity for mask ID: " + item.getMaskId());
            }

            maskInfo.setPurchaseQuantity(item.getQuantity());
            BigDecimal itemTotal = maskInfo.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            maskInfo.setTotalPrice(itemTotal);
            totalAmount = totalAmount.add(itemTotal);

            maskInfos.add(maskInfo);
        }

        if(userBalance.compareTo(totalAmount) < 0) {
            throw new IllegalArgumentException(
                String.format("Insufficient funds. Required: %s, Available: %s",
                    totalAmount, userBalance));
        }

        LocalDateTime purchaseDateTime = LocalDateTime.now();

        // execute transactions: subtract user balance, update pharmacy balance, create transaction records
        purchaseDao.updateUserBalance(userId, userBalance.subtract(totalAmount));

        Map<Integer, List<MaskInfoDto>> storeGroups = maskInfos.stream()
            .collect(Collectors.groupingBy(MaskInfoDto::getStoreId));

        for(Map.Entry<Integer, List<MaskInfoDto>> entry : storeGroups.entrySet()) {
            int storeId = entry.getKey();
            BigDecimal storeEarnings = entry.getValue().stream()
                .map(MaskInfoDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            purchaseDao.updatePharmacyBalance(storeId, storeEarnings);
        }

        for (MaskInfoDto maskInfo : maskInfos) {
            purchaseDao.createTransaction(
                userId,
                maskInfo.getStoreId(),
                maskInfo.getMaskId(),
                maskInfo.getTotalPrice(),
                purchaseDateTime
            );
        }

        // build response based on the purchase details
        BigDecimal remainingBalance = userBalance.subtract(totalAmount);

        List<PurchasedItemDto> purchasedItems = maskInfos.stream()
            .map(maskInfo -> {
                PurchasedItemDto purchasedItem = new PurchasedItemDto();
                purchasedItem.setMaskId(maskInfo.getMaskId());
                purchasedItem.setMaskName(maskInfo.getMaskName());
                purchasedItem.setPharmacyName(maskInfo.getStoreName());
                purchasedItem.setQuantity(maskInfo.getPurchaseQuantity());
                purchasedItem.setUnitPrice(maskInfo.getPrice());
                purchasedItem.setTotalPrice(maskInfo.getTotalPrice());
                return purchasedItem;
            })
            .collect(Collectors.toList());

        PurchaseResponseDto response = new PurchaseResponseDto();
        response.setMessage("Purchase completed successfully");
        response.setTotalAmount(totalAmount);
        response.setRemainingBalance(remainingBalance);
        response.setPurchaseDateTime(purchaseDateTime);
        response.setPurchasedItems(purchasedItems);

        log.info("Purchase completed for user {} with total amount {}", userId, totalAmount);
        return response;
    }



    private void validatePurchaseRequest(PurchaseRequestDto purchaseRequest) {
        if (purchaseRequest == null) {
            throw new IllegalArgumentException("Purchase request cannot be null");
        }

        if (purchaseRequest.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (purchaseRequest.getItems() == null || purchaseRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Purchase items cannot be empty");
        }

        // Check for duplicate mask IDs
        List<Integer> maskIds = purchaseRequest.getItems().stream()
            .map(PurchaseItemDto::getMaskId)
            .toList();

        if (maskIds.size() != maskIds.stream().distinct().count()) {
            throw new IllegalArgumentException("Duplicate mask IDs found in purchase request");
        }
    }
}

