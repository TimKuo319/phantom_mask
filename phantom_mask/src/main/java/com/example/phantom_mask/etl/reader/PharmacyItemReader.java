package com.example.phantom_mask.etl.reader;

import com.example.phantom_mask.etl.model.Pharmacy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemReader;

import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class PharmacyItemReader implements ItemReader<Pharmacy> {

    private final Iterator<Pharmacy> iterator;

    public PharmacyItemReader(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = Paths.get(filePath).toFile();
            List<Pharmacy> pharmacies = objectMapper.readValue(jsonFile, new TypeReference<>() {});
            this.iterator = pharmacies.iterator();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON file: " + filePath, e);
        }
    }

    @Override
    public Pharmacy read() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}