package com.example.phantom_mask.service.user;

import com.example.phantom_mask.dao.user.UserDao;
import com.example.phantom_mask.dto.UserDto;
import com.example.phantom_mask.etl.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Log4j2
@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<UserDto> getTopUserByTransactionCount(String startDate, String endDate, int limit) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        try {
            LocalDate queryStartDate = LocalDate.parse(startDate, formatter);
            LocalDate queryEndDate = LocalDate.parse(endDate, formatter);

            if(queryStartDate.isAfter(queryEndDate)) {
                throw new IllegalArgumentException("startDate must be before endDate.");
            }


            /*
             * Convert LocalDate to LocalDateTime to define the full timestamp range
             * for accurate comparison in the database query.
             */
            LocalDateTime startDateTime = queryStartDate.atStartOfDay();
            LocalDateTime endDateTime = queryEndDate.atTime(LocalTime.MAX);

            return userDao.getTopUserByTransactionCount(startDateTime, endDateTime, limit);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Please use ISO 8601 format (e.g., 2025-01-01T00:00:00).", e);
        }
    }
}
