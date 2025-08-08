package com.example.phantom_mask.service.search;

import com.example.phantom_mask.dao.search.SearchDao;
import com.example.phantom_mask.dto.MaskSearchDto;
import com.example.phantom_mask.dto.StoreSearchDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Log4j2
@Service
public class SearchServiceImpl implements SearchService {

    private static final int MAX_LIMIT = 100;

    private final SearchDao searchDao;

    public SearchServiceImpl(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    @Override
    public List<StoreSearchDto> searchStoresByName(String q, int limit, int offset) {
        String query = sanitizeQuery(q);
        int safeLimit  = normalizeLimit(limit);
        int safeOffset = Math.max(0, offset);

        return searchDao.searchStoresByNameFulltext(query, safeLimit, safeOffset);
    }

    @Override
    public List<MaskSearchDto> searchMasksByName(String q, int limit, int offset) {
        String query = sanitizeQuery(q);
        int safeLimit  = normalizeLimit(limit);
        int safeOffset = Math.max(0, offset);

        return searchDao.searchMasksByNameFulltext(query, safeLimit, safeOffset);
    }

    private String sanitizeQuery(String q) {
        if (!StringUtils.hasText(q)) {
            throw new IllegalArgumentException("Query q must not be empty.");
        }
        String trimmed = q.trim().replaceAll("\\s+", " ");
        if (trimmed.length() > 200) {
            trimmed = trimmed.substring(0, 200);
        }
        return trimmed;
    }

    private int normalizeLimit(int limit) {
        if (limit <= 0) return 20;
        return Math.min(limit, MAX_LIMIT);
    }

}
