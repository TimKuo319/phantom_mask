package com.example.phantom_mask.service.analytics;

import com.example.phantom_mask.dto.MaskTransactionStatsDto;

public interface AnalyticsService {

    MaskTransactionStatsDto getMaskTransactionStats(String startDate, String endDate);
}
