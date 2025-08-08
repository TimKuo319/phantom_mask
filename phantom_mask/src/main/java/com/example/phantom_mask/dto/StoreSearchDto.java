package com.example.phantom_mask.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreSearchDto {
    private int id;
    private String name;

    @JsonIgnore
    private BigDecimal relevantScore;


}
