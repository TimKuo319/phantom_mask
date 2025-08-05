package com.example.phantom_mask.etl.model;

import com.example.phantom_mask.model.OpeningHour;
import com.example.phantom_mask.model.Pharmacy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProcessedPharmacy {
    private Pharmacy pharmacy;
    private List<OpeningHour> openingHours;

}
