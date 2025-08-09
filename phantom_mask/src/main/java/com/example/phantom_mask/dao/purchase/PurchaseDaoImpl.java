package com.example.phantom_mask.dao.purchase;

import com.example.phantom_mask.dto.purchase.MaskInfoDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Repository
public class PurchaseDaoImpl implements PurchaseDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PurchaseDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getUserBalance(int userId) {
        String sql = "SELECT cash_balance FROM users WHERE id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        try{
            return jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
        } catch (Exception e) {
            log.warn("User not found with ID: {}", userId);
            return null;
        }


    }
    @Override
    public MaskInfoDto getMaskInfo(int maskId) {
        String sql = """
            SELECT m.id as mask_id, m.name as mask_name, m.price,
                   s.id as store_id, s.name as store_name
            FROM masks m
            JOIN stores s ON m.store_id = s.id
            WHERE m.id = :maskId
            """;

        Map<String, Object> params = new HashMap<>();
        params.put("maskId", maskId);

        try {
            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                MaskInfoDto maskInfo = new MaskInfoDto();
                maskInfo.setMaskId(rs.getInt("mask_id"));
                maskInfo.setMaskName(rs.getString("mask_name"));
                maskInfo.setPrice(rs.getBigDecimal("price"));
                maskInfo.setStoreId(rs.getInt("store_id"));
                maskInfo.setStoreName(rs.getString("store_name"));
                return maskInfo;
            });
        } catch (Exception e) {
            log.warn("Mask not found with ID: {}", maskId);
            return null;
        }

    }

    @Override
    public void updateUserBalance(int userId, BigDecimal newBalance) {
        String sql = "UPDATE users SET cash_balance = :newBalance WHERE id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("newBalance", newBalance);
        params.put("userId", userId);

        int rowsUpdated = jdbcTemplate.update(sql, params);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Failed to update user balance. User ID: " + userId);
        }
    }


    @Override
    public void updatePharmacyBalance(int storeId, BigDecimal additionalAmount) {
        String sql = "UPDATE stores SET cash_balance = cash_balance + :additionalAmount WHERE id = :store";

        Map<String, Object> params = new HashMap<>();
        params.put("additionalAmount", additionalAmount);
        params.put("store", storeId);

        int rowsUpdated = jdbcTemplate.update(sql, params);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Failed to update store balance. Store ID: " + storeId);
        }
    }

    @Override
    public void createTransaction(int userId, int storeId, int maskId, BigDecimal transactionAmount, LocalDateTime transactionDate) {
        String sql = """
            INSERT INTO transactions (user_id, store_id, mask_id, transaction_amount, transaction_date)
            VALUES (:userId, :storeId, :maskId, :transactionAmount, :transactionDate)
            """;

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("storeId", storeId);
        params.put("maskId", maskId);
        params.put("transactionAmount", transactionAmount);
        params.put("transactionDate", transactionDate);

        int rowsInserted = jdbcTemplate.update(sql, params);
        if (rowsInserted == 0) {
            throw new IllegalArgumentException("Failed to create transaction record");
        }
    }

}
