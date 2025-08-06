package com.example.phantom_mask.service.Pharmacy;

import com.example.phantom_mask.dto.OpenPharmacyDto;

import java.time.LocalTime;
import java.util.List;

public interface PharmacyService {

    List<OpenPharmacyDto> getOpenPharmacies(String time, String dayOfWeek);

}
