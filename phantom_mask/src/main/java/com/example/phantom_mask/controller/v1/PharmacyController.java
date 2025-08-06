package com.example.phantom_mask.controller.v1;

import com.example.phantom_mask.dto.OpenPharmacyDto;
import com.example.phantom_mask.service.Pharmacy.PharmacyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/1.0/pharmacy")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    /**
     * Get open pharmacies based on the provided time and optional day of the week.
     *
     * @param time The time in HH:mm format to check for open pharmacies.
     * @param dayOfWeek Optional day of the week to filter pharmacies (e.g., "Monday", "Tuesday").
     * @return A ResponseEntity containing a list of OpenPharmacyDto objects or an error message.
     */

    @GetMapping("/open")
    public ResponseEntity<?> getOpenPharmacies(
        @RequestParam("time") String time,
        @RequestParam(value = "dayOfWeek", required = false) String dayOfWeek
        ) {
            try {
                List<OpenPharmacyDto> openPharmacies = pharmacyService.getOpenPharmacies(time, dayOfWeek);

                Map<String, Object> response = new HashMap<>();
                response.put("data", openPharmacies);
                return ResponseEntity.ok(response);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error",e.getMessage() ));
            }
    }
}
