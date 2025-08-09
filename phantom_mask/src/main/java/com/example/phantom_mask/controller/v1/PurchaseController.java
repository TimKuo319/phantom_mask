package com.example.phantom_mask.controller.v1;

import com.example.phantom_mask.dto.purchase.PurchaseRequestDto;
import com.example.phantom_mask.dto.purchase.PurchaseResponseDto;
import com.example.phantom_mask.service.purchase.PurchaseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("api/1.0/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/masks")
    public ResponseEntity<?> purchaseMasks(
        @RequestBody PurchaseRequestDto purchaseRequest
    ) {
        try {
            PurchaseResponseDto response = purchaseService.processPurchase(purchaseRequest);
            return ResponseEntity.ok(Map.of("data", response));
        } catch (IllegalArgumentException e) {
            log.warn("Purchase failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error during purchase", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred while processing your purchase."));
        }
    }
}
