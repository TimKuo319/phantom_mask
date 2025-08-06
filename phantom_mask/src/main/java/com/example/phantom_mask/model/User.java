package com.example.phantom_mask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class User {
    private String name;
    private BigDecimal cashBalance;

    @JsonProperty("purchaseHistories")
    private List<Transaction> transactions;
}
