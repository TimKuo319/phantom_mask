package com.example.phantom_mask.service.Pharmacy;

import com.example.phantom_mask.dto.FilteredPharmacyDto;
import com.example.phantom_mask.dto.MaskDto;
import com.example.phantom_mask.dto.OpenPharmacyDto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public interface PharmacyService {

    List<OpenPharmacyDto> getOpenPharmacies(String time, String dayOfWeek);
    List<MaskDto> getMasksByPharmacyId(int pharmacyId, String sortBy, String order);
    List<FilteredPharmacyDto> getFilteredPharmacies(BigDecimal minPrice, BigDecimal maxPrice, int threshold, String comparison);
}
