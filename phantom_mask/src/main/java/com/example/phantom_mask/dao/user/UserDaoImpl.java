package com.example.phantom_mask.dao.user;


import com.example.phantom_mask.dto.UserDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserDto> getTopUserByTransactionCount(LocalDateTime startDate, LocalDateTime endDate, int limit) {

        String sql = """
            SELECT u.id, u.name, u.cash_balance, SUM(t.transaction_amount) AS total_spent
            FROM users u
                    JOIN transactions t ON u.id = t.user_id
                    WHERE t.transaction_date BETWEEN :startDate AND :endDate
                    GROUP BY u.id, u.name, u.cash_balance
                    ORDER BY total_spent DESC
                    LIMIT :limit
            """;

        Map<String, Object> params = Map.of(
            "startDate", Timestamp.valueOf(startDate),
            "endDate", Timestamp.valueOf(endDate),
            "limit", limit
        );

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            UserDto dto = new UserDto();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));
            dto.setCashBalance(rs.getBigDecimal("cash_balance"));
            dto.setTotalSpent(rs.getBigDecimal("total_spent"));
            return dto;
        });
    }

}
