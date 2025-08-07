package com.example.phantom_mask.etl.writer;

import com.example.phantom_mask.etl.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;

@Log4j2
public class PharmacyWriter implements ItemWriter<ProcessedPharmacy> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PharmacyWriter(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(Chunk<? extends ProcessedPharmacy> chunk) throws Exception {
        for (ProcessedPharmacy processed : chunk) {
            Pharmacy pharmacy = processed.getPharmacy();

            String insertStoreSql = """
                INSERT INTO stores (name, cash_balance, raw_opening_hours)
                VALUES (:name, :balance, :raw)
                ON DUPLICATE KEY UPDATE
                    cash_balance = VALUES(cash_balance),
                    raw_opening_hours = VALUES(raw_opening_hours)
                """;

            MapSqlParameterSource storeParams = new MapSqlParameterSource()
                .addValue("name", pharmacy.getName())
                .addValue("balance", pharmacy.getCashBalance())
                .addValue("raw", pharmacy.getOpeningHours());
            jdbcTemplate.update(insertStoreSql, storeParams);

            Integer storeId = jdbcTemplate.queryForObject(
                "SELECT id FROM stores WHERE name = :name",
                new MapSqlParameterSource("name", pharmacy.getName()),
                Integer.class
            );

            for (OpeningHour oh : processed.getOpeningHours()) {
                String insertOHSql = """
                    INSERT INTO opening_hours (store_id, day_of_week, start_time, end_time, is_cross_night)
                    VALUES (:storeId, :day, :start, :end, :cross)
                    ON DUPLICATE KEY UPDATE
                        is_cross_night = VALUES(is_cross_night)
                    """;
                MapSqlParameterSource ohParams = new MapSqlParameterSource()
                    .addValue("storeId", storeId)
                    .addValue("day", oh.getDayOfWeek())
                    .addValue("start", oh.getStartTime())
                    .addValue("end", oh.getEndTime())
                    .addValue("cross", oh.isCrossNight());
                jdbcTemplate.update(insertOHSql, ohParams);
            }

            for (ProcessedMask mask : processed.getMasks()) {
                String insertMaskSql = """
                    INSERT INTO masks (store_id, name, price, quantity)
                    VALUES (:storeId, :name, :price, :quantity)
                    ON DUPLICATE KEY UPDATE
                        price = VALUES(price),
                        quantity = VALUES(quantity)
                    """;
                MapSqlParameterSource maskParams = new MapSqlParameterSource()
                    .addValue("storeId", storeId)
                    .addValue("name", mask.getName())
                    .addValue("price", mask.getPrice())
                    .addValue("quantity", mask.getQuantity());

                jdbcTemplate.update(insertMaskSql, maskParams);
            }
        }
    }
}