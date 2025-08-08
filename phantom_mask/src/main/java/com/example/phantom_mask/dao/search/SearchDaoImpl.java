package com.example.phantom_mask.dao.search;

import com.example.phantom_mask.dto.MaskSearchDto;
import com.example.phantom_mask.dto.StoreSearchDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SearchDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<StoreSearchDto> searchStoresByNameFulltext(String q, int limit, int offset) {
        String sql = """
            SELECT s.id, s.name,
                   MATCH(s.name) AGAINST (:q IN BOOLEAN MODE) AS score
            FROM stores s
            WHERE MATCH(s.name) AGAINST (:q IN BOOLEAN MODE)
            ORDER BY score DESC
            LIMIT :limit OFFSET :offset
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        params.put("limit", limit);
        params.put("offset", offset);

        return jdbcTemplate.query(sql, params, (rs, rn) -> {
            StoreSearchDto dto = new StoreSearchDto();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));
            dto.setRelevantScore(rs.getBigDecimal("score"));
            return dto;
        });
    }

    @Override
    public List<MaskSearchDto> searchMasksByNameFulltext(String q, int limit, int offset) {
        String sql = """
            SELECT m.id, m.name, m.price, s.id AS store_id, s.name AS store_name,
                   MATCH(m.name) AGAINST (:q IN BOOLEAN MODE) AS score
            FROM masks m
            JOIN stores s ON s.id = m.store_id
            WHERE MATCH(m.name) AGAINST (:q IN BOOLEAN MODE)
            ORDER BY score DESC
            LIMIT :limit OFFSET :offset
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        params.put("limit", limit);
        params.put("offset", offset);

        return jdbcTemplate.query(sql, params, (rs, rn) -> {
            MaskSearchDto dto = new MaskSearchDto();
            dto.setId(rs.getInt("id"));
            dto.setName(rs.getString("name"));
            dto.setPrice(rs.getBigDecimal("price"));
            dto.setStoreId(rs.getInt("store_id"));
            dto.setStoreName(rs.getString("store_name"));
            dto.setRelevantScore(rs.getBigDecimal("score"));
            return dto;
        });
    }
}
