package com.example.phantom_mask.dao.analytics;

import com.example.phantom_mask.dto.MaskTransactionStatsDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class AnalyticsDaoImpl implements AnalyticsDao{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AnalyticsDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MaskTransactionStatsDto getMaskTransactionStats(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
            SELECT 
                SUM(m.quantity) AS total_mask_count,
                SUM(t.transaction_amount) AS total_transaction_amount
            FROM transactions t
            JOIN masks m ON t.mask_id = m.id
            WHERE t.transaction_date BETWEEN :start AND :end
        """;

        Map<String, Object> params = Map.of(
            "start", Timestamp.valueOf(startDate),
            "end", Timestamp.valueOf(endDate)
        );

        return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
            MaskTransactionStatsDto stats = new MaskTransactionStatsDto();
            stats.setTotalMaskCount(rs.getInt("total_mask_count"));
            stats.setTotTransactionAmount(rs.getBigDecimal("total_transaction_amount"));
            return stats;
        });

    }

}
