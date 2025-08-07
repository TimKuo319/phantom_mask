package com.example.phantom_mask.dao.user;

import com.example.phantom_mask.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserDao {

    List<UserDto> getTopUserByTransactionCount(LocalDateTime startDate, LocalDateTime endDate, int limit);
}
