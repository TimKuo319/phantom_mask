package com.example.phantom_mask.etl.reader;

import com.example.phantom_mask.etl.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemReader;

import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class UserItemReader implements ItemReader<User> {

    private final Iterator<User> iterator;

    public UserItemReader(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper()
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            File file = Paths.get(filePath).toFile();
            List<User> users = mapper.readValue(file, new TypeReference<>() {});
            this.iterator = users.iterator();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON file: " + filePath, e);
        }
    }

    @Override
    public User read() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
