package com.example.phantom_mask.controller.v1;

import com.example.phantom_mask.dto.MaskDto;
import com.example.phantom_mask.dto.OpenPharmacyDto;
import com.example.phantom_mask.service.Pharmacy.PharmacyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Get masks available in a specific pharmacy.
     *
     * @param pharmacyId The ID of the pharmacy to retrieve masks from.
     * @param sortBy The field to sort the masks by (default is "name").
     * @param order The order of sorting (default is "desc").
     * @return A ResponseEntity containing a list of MaskDto objects
     */
    @GetMapping("/{pharmacyId}/masks")
    public ResponseEntity<?> getMaskByPharmacyId(
        @PathVariable int pharmacyId,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "order", defaultValue = "desc") String order
    ) {
        List<MaskDto> masks = pharmacyService.getMasksByPharmacyId(pharmacyId, sortBy, order);
        Map<String, Object> response = new HashMap<>();
        response.put("data", masks);
        return ResponseEntity.ok(response);
    }
}
