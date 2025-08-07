package com.example.phantom_mask.etl.processor;

import com.example.phantom_mask.etl.model.ProcessedMask;
import com.example.phantom_mask.etl.model.ProcessedPharmacy;
import com.example.phantom_mask.etl.model.OpeningHour;
import com.example.phantom_mask.etl.model.Pharmacy;
import com.example.phantom_mask.util.OpeningHourParser;
import com.example.phantom_mask.util.QuantityParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

@Log4j2
public class PharmacyProcessor implements ItemProcessor<Pharmacy, ProcessedPharmacy> {

    @Override
    public ProcessedPharmacy process(Pharmacy pharmacy) throws Exception {
        List<OpeningHour> openingHours = OpeningHourParser
            .parse(pharmacy.getOpeningHours());

        List<ProcessedMask> processedMasks = pharmacy.getMasks().stream()
            .map(mask -> {
                int quantity = QuantityParser.parseQuantity(mask.getName());
                return new ProcessedMask(mask.getName(), mask.getPrice(), quantity);
            })
            .toList();

        return new ProcessedPharmacy(pharmacy, openingHours, processedMasks);
    }
}
