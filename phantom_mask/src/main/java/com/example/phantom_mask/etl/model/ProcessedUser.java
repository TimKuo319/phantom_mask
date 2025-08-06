package com.example.phantom_mask.etl.model;

import com.example.phantom_mask.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProcessedUser {
    private final User user;
}
