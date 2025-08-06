package com.example.phantom_mask.dao;

import com.example.phantom_mask.dto.OpenPharmacyDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
public class PharmacyDaoImpl implements PharmacyDao{

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
}
