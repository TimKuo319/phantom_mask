package com.example.phantom_mask.etl.writer;

import com.example.phantom_mask.etl.model.ProcessedPharmacy;
import com.example.phantom_mask.model.Mask;
import com.example.phantom_mask.model.OpeningHour;
import com.example.phantom_mask.model.Pharmacy;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;

public class PharmacyWriter implements ItemWriter<ProcessedPharmacy> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PharmacyWriter(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(Chunk<? extends ProcessedPharmacy> chunk) throws Exception {
        for (ProcessedPharmacy processed : chunk) {
            Pharmacy pharmacy = processed.getPharmacy();

            String insertStoreSql = "INSERT INTO stores (name, cash_balance, raw_opening_hours) " +
                "VALUES (:name, :balance, :raw)";
            MapSqlParameterSource storeParams = new MapSqlParameterSource()
                .addValue("name", pharmacy.getName())
                .addValue("balance", pharmacy.getCashBalance())
                .addValue("raw", pharmacy.getOpeningHours());
            jdbcTemplate.update(insertStoreSql, storeParams);

            Integer storeId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", new HashMap<>(), Integer.class);

            for (OpeningHour oh : processed.getOpeningHours()) {
                String insertOHSql = "INSERT INTO opening_hours (store_id, day_of_week, start_time, end_time, is_cross_night) " +
                    "VALUES (:storeId, :day, :start, :end, :cross)";
                MapSqlParameterSource ohParams = new MapSqlParameterSource()
                    .addValue("storeId", storeId)
                    .addValue("day", oh.getDayOfWeek())
                    .addValue("start", oh.getStartTime())
                    .addValue("end", oh.getEndTime())
                    .addValue("cross", oh.isCrossNight());
                jdbcTemplate.update(insertOHSql, ohParams);
            }

            for (Mask mask : pharmacy.getMasks()) {
                String insertMaskSql = "INSERT INTO masks (store_id, name, price) " +
                    "VALUES (:storeId, :name, :price)";
                MapSqlParameterSource maskParams = new MapSqlParameterSource()
                    .addValue("storeId", storeId)
                    .addValue("name", mask.getName())
                    .addValue("price", mask.getPrice());
                jdbcTemplate.update(insertMaskSql, maskParams);
            }
        }
    }
}