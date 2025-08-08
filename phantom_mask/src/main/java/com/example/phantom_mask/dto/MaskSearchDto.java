package com.example.phantom_mask.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaskSearchDto {
    private int id;
    private String name;
    private BigDecimal price;
    private int storeId;
    private String storeName;

    @JsonIgnore
    private BigDecimal relevantScore;
}
