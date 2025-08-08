package com.example.phantom_mask.service.search;

import com.example.phantom_mask.dto.MaskSearchDto;
import com.example.phantom_mask.dto.StoreSearchDto;

import java.util.List;

public interface SearchService {
    List<StoreSearchDto> searchStoresByName(String q, int limit, int offset);
    List<MaskSearchDto>  searchMasksByName(String q, int limit, int offset);

}
