package com.example.phantom_mask.service.analytics;

import com.example.phantom_mask.dao.analytics.AnalyticsDao;
import com.example.phantom_mask.dto.MaskTransactionStatsDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Log4j2
@Service
public class AnalyticsServiceImpl implements AnalyticsService{

    private final AnalyticsDao analyticsDao;

    public AnalyticsServiceImpl(AnalyticsDao analyticsDao) {
        this.analyticsDao = analyticsDao;
    }

    public MaskTransactionStatsDto getMaskTransactionStats(String startDate, String endDate) {

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

            return analyticsDao.getMaskTransactionStats(startDateTime, endDateTime);

        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Please use ISO 8601 Date format (e.g., 2025-01-01).", e);
        }

    }
}
