package com.example.phantom_mask.etl.processor;

import com.example.phantom_mask.etl.model.ProcessedUser;
import com.example.phantom_mask.model.User;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User, ProcessedUser> {
    @Override
    public ProcessedUser process(User user) {
        return new ProcessedUser(user);
    }
}
