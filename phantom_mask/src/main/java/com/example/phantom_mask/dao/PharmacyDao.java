package com.example.phantom_mask.dao;


import com.example.phantom_mask.dto.FilteredPharmacyDto;
import com.example.phantom_mask.dto.MaskDto;
import com.example.phantom_mask.dto.OpenPharmacyDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public interface PharmacyDao {
    List<OpenPharmacyDto> getOpenPharmacies(String dayOfWeek, LocalTime time);
    List<MaskDto> getMasksByPharmacyId(int pharmacyId, String sortBy, String order);
    List<FilteredPharmacyDto> getFilteredPharmacies(BigDecimal minPrice, BigDecimal maxPrice, int threshold, String operator);
}
