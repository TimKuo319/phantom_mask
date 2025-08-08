package com.example.phantom_mask.dao.analytics;

import com.example.phantom_mask.dto.MaskTransactionStatsDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AnalyticsDao {

    MaskTransactionStatsDto getMaskTransactionStats(LocalDateTime startDate, LocalDateTime endDate);
}
