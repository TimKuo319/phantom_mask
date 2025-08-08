package com.example.phantom_mask.dao.search;

import com.example.phantom_mask.dto.MaskSearchDto;
import com.example.phantom_mask.dto.StoreSearchDto;

import java.util.List;

public interface SearchDao {

    List<StoreSearchDto> searchStoresByNameFulltext(String q, int limit, int offset);
    List<MaskSearchDto>  searchMasksByNameFulltext(String q, int limit, int offset);
}
