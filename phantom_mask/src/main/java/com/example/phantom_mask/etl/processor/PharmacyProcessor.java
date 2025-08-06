package com.example.phantom_mask.etl.processor;

import com.example.phantom_mask.etl.model.ProcessedPharmacy;
import com.example.phantom_mask.etl.model.OpeningHour;
import com.example.phantom_mask.etl.model.Pharmacy;
import com.example.phantom_mask.util.OpeningHourParser;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class PharmacyProcessor implements ItemProcessor<Pharmacy, ProcessedPharmacy> {

    @Override
    public ProcessedPharmacy process(Pharmacy pharmacy) throws Exception {
        List<OpeningHour> openingHours = OpeningHourParser
            .parse(pharmacy.getOpeningHours());

        return new ProcessedPharmacy(pharmacy, openingHours);
    }
}
