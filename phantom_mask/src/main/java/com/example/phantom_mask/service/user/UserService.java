package com.example.phantom_mask.service.user;

import com.example.phantom_mask.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getTopUserByTransactionCount(String startDate, String endDate, int limit);
}
