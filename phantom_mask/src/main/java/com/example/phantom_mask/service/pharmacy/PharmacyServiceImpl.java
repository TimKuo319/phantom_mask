package com.example.phantom_mask.service.pharmacy;

import com.example.phantom_mask.dao.PharmacyDao;
import com.example.phantom_mask.dto.FilteredPharmacyDto;
import com.example.phantom_mask.dto.MaskDto;
import com.example.phantom_mask.dto.OpenPharmacyDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Log4j2
@Service
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyDao pharmacyDao;

    private static final Set<String> VALID_DAYS = Set.of(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    );

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "price");
    private static final Set<String> ALLOWED_SORT_ORDERS = Set.of("asc", "desc");
    private static final Set<String> ALLOWED_COMPARISONS = Set.of("more", "less");

    public PharmacyServiceImpl(PharmacyDao pharmacyDao) {
        this.pharmacyDao = pharmacyDao;
    }

    @Override
    public List<OpenPharmacyDto> getOpenPharmacies(String time, String dayOfWeek) throws IllegalArgumentException{

        LocalTime queryTime;

        // Validate and parse the time, dayOfWeek parameters
        try {
            queryTime = LocalTime.parse(time);
        } catch (Exception e) {
            log.error("Invalid time format: {}", time, e);
            throw new IllegalArgumentException("Invalid time format. Please use HH:mm.");
        }

        String targetDay = dayOfWeek != null ? dayOfWeek :
            LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);


        if (!VALID_DAYS.contains(targetDay)) {
            throw new IllegalArgumentException("Invalid dayOfWeek. Must be Mon, Tue, ..., Sun");
        }


        List<OpenPharmacyDto> rawResults = pharmacyDao.getOpenPharmacies(targetDay, queryTime);

        for (OpenPharmacyDto dto : rawResults) {
            dto.setTodayHours(dto.getStartTime().toString() + " - " + dto.getEndTime().toString());

            Duration duration = calculateClosingTime(queryTime, dto);

            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            dto.setClosesIn(String.format("%d hours %d minutes", hours, minutes));
        }

        return rawResults;
    }

    public List<MaskDto> getMasksByPharmacyId(int pharmacyId, String sortBy, String order) {
        // Validate sortBy and order to prevent malicious input
        String sanitizedSortBy = ALLOWED_SORT_FIELDS.contains(sortBy.toLowerCase()) ? sortBy.toLowerCase() : "name";
        String sanitizedOrder = ALLOWED_SORT_ORDERS.contains(order.toLowerCase()) ? order.toLowerCase() : "asc";

        return pharmacyDao.getMasksByPharmacyId(pharmacyId, sanitizedSortBy, sanitizedOrder);
    }

    public List<FilteredPharmacyDto> getFilteredPharmacies(BigDecimal minPrice, BigDecimal maxPrice, int threshold, String comparison) {
        // Validate comparison to prevent malicious input
        String sanitizedComparison = ALLOWED_COMPARISONS.contains(comparison.toLowerCase()) ? comparison.toLowerCase() : "more";
        String operator = sanitizedComparison.equals("more") ? ">" : "<";
        if(maxPrice == null) {
            maxPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        }

        return pharmacyDao.getFilteredPharmacies(minPrice, maxPrice, threshold, operator);
    }

    private Duration calculateClosingTime(LocalTime queryTime, OpenPharmacyDto dto) {
        LocalTime startTime = dto.getStartTime();
        LocalTime endTime = dto.getEndTime();
        boolean isCrossNight = dto.isCrossNight();

        if (!isCrossNight) {
            return Duration.between(queryTime, endTime);
        } else {
            if (queryTime.compareTo(startTime) >= 0) {

                Duration toMidnight = Duration.between(queryTime, LocalTime.MAX).plus(Duration.ofNanos(1));
                Duration fromMidnightToEnd = Duration.between(LocalTime.MIN, endTime);
                return toMidnight.plus(fromMidnightToEnd);
            } else {

                return Duration.between(queryTime, endTime);
            }
        }
    }
}
