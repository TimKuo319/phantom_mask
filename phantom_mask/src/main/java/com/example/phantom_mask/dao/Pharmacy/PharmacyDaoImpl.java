package com.example.phantom_mask.dao.Pharmacy;

import com.example.phantom_mask.dto.FilteredPharmacyDto;
import com.example.phantom_mask.dto.MaskDto;
import com.example.phantom_mask.dto.OpenPharmacyDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Log4j2
@Repository
public class PharmacyDaoImpl implements PharmacyDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PharmacyDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OpenPharmacyDto> getOpenPharmacies(String dayOfWeek, LocalTime time) {
        String sql = """
            SELECT s.id, s.name, oh.start_time, oh.end_time, oh.is_cross_night
            FROM opening_hours oh
            JOIN stores s ON oh.store_id = s.id
            WHERE oh.day_of_week = :dayOfWeek
              AND (
                   (oh.is_cross_night = false AND :time BETWEEN oh.start_time AND oh.end_time)
                   OR (oh.is_cross_night = true AND (
                        :time >= oh.start_time OR :time < oh.end_time
                   ))
              )
        """;

        Map<String, Object> params = Map.of(
            "dayOfWeek", dayOfWeek,
            "time", Time.valueOf(time)
        );

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            OpenPharmacyDto dto = new OpenPharmacyDto();
            dto.setId(rs.getLong("id"));
            dto.setName(rs.getString("name"));
            dto.setStartTime(rs.getTime("start_time").toLocalTime());
            dto.setEndTime(rs.getTime("end_time").toLocalTime());
            dto.setCrossNight(rs.getBoolean("is_cross_night"));
            return dto;
        });
    }

    @Override
    public List<MaskDto> getMasksByPharmacyId(int pharmacyId, String sortBy, String order) {
        String sql = String.format("""
            SELECT m.id, m.name, m.price
            FROM masks m
            JOIN stores s ON m.store_id = s.id
            WHERE s.id = :pharmacyId
            ORDER BY %s %s
        """, sortBy, order);

        Map<String, Object> params = Map.of(
            "pharmacyId", pharmacyId
        );

        log.info(sortBy);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            MaskDto dto = new MaskDto();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));
            dto.setPrice(rs.getBigDecimal("price"));
            return dto;
        });
    }

    @Override
    public List<FilteredPharmacyDto> getFilteredPharmacies(BigDecimal minPrice, BigDecimal maxPrice, int threshold, String operator) {
        String sql = String.format("""
        SELECT s.id, s.name, s.cash_balance, COUNT(m.id) as mask_count
        FROM stores s
        JOIN masks m ON s.id = m.store_id
        WHERE m.price BETWEEN :minPrice AND :maxPrice
        GROUP BY s.id
        HAVING COUNT(m.id) %s :threshold
    """, operator);

        Map<String, Object> params = Map.of(
            "minPrice", minPrice,
            "maxPrice", maxPrice,
            "threshold", threshold
        );

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            FilteredPharmacyDto dto = new FilteredPharmacyDto();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));
            dto.setCashBalance(rs.getBigDecimal("cash_balance"));
            dto.setMatchedMasksCount(rs.getInt("mask_count"));
            return dto;
        });
    }
}
