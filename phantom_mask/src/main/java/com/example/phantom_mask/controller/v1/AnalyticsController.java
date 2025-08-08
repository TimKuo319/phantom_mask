package com.example.phantom_mask.controller.v1;

import com.example.phantom_mask.dto.MaskTransactionStatsDto;
import com.example.phantom_mask.service.analytics.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/1.0/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/masks/transaction-stats")
    public ResponseEntity<?> getMaskTransactionStats(
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate
    ) {

        try{
            MaskTransactionStatsDto stats = analyticsService.getMaskTransactionStats(startDate, endDate);
            Map<String, Object> response = new HashMap<>();
            response.put("data", stats);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
