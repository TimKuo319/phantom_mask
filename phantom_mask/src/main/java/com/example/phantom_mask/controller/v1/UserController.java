package com.example.phantom_mask.controller.v1;


import com.example.phantom_mask.dto.UserDto;
import com.example.phantom_mask.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("api/1.0/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get top users by transaction amount within a specified date range.
     *
     * @param startDate The start date in ISO 8601 format (e.g., "2025-01-01").
     * @param endDate The end date in ISO 8601 format (e.g., "2025-01-31").
     * @param threshold The minimum number of transactions to consider a user as top user (default is 1).
     * @return A ResponseEntity containing a list of UserDto objects or an error message.
     */


    @GetMapping("/top-users")
    public ResponseEntity<?> getTopUsersByTransactionAmount(
        @RequestParam(value = "startDate") String startDate,
        @RequestParam("endDate") String endDate,
        @RequestParam(value = "threshold", defaultValue = "1") int threshold
    ) {
        try {
            List<UserDto> topUsers = userService.getTopUserByTransactionCount(startDate, endDate, threshold);
            Map<String, Object> response = new HashMap<>();
            response.put("data", topUsers);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
