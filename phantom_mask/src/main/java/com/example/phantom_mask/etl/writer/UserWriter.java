package com.example.phantom_mask.etl.writer;


import com.example.phantom_mask.etl.model.ProcessedUser;
import com.example.phantom_mask.model.Transaction;
import com.example.phantom_mask.model.User;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;

public class UserWriter implements ItemWriter<ProcessedUser> {

    private final NamedParameterJdbcTemplate jdbc;

    public UserWriter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void write(Chunk<? extends ProcessedUser> chunk) throws Exception {
        for (ProcessedUser pu : chunk) {
            User user = pu.getUser();

            String insUser = "INSERT INTO users (name, cash_balance) VALUES (:name, :cb)";
            jdbc.update(insUser, new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("cb", user.getCashBalance()));
            Integer userId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", new HashMap<>(), Integer.class);

            for (Transaction tx : user.getTransactions()) {
                Integer storeId = jdbc.queryForObject(
                    "SELECT id FROM stores WHERE name = :name",
                    new MapSqlParameterSource("name", tx.getPharmacyName()), Integer.class);

                Integer maskId = jdbc.queryForObject(
                    "SELECT m.id FROM masks m JOIN stores s ON m.store_id = s.id " +
                        "WHERE s.name = :sname AND m.name = :mname",
                    new MapSqlParameterSource()
                        .addValue("sname", tx.getPharmacyName())
                        .addValue("mname", tx.getMaskName()),
                    Integer.class);

                String insTx = "INSERT INTO transactions (user_id, store_id, mask_id, transaction_amount, transaction_date) " +
                    "VALUES (:u, :s, :m, :amt, :dt)";
                jdbc.update(insTx, new MapSqlParameterSource()
                    .addValue("u", userId)
                    .addValue("s", storeId)
                    .addValue("m", maskId)
                    .addValue("amt", tx.getTransactionAmount())
                    .addValue("dt", tx.getTransactionDate()));
            }
        }
    }
}