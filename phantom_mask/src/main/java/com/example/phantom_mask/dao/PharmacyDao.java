package com.example.phantom_mask.dao;


import com.example.phantom_mask.dto.OpenPharmacyDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalTime;
import java.util.List;

public interface PharmacyDao {
    List<OpenPharmacyDto> getOpenPharmacies(String dayOfWeek, LocalTime time);

}
