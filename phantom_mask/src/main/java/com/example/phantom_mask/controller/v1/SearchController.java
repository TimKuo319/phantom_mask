package com.example.phantom_mask.controller.v1;

import com.example.phantom_mask.dto.MaskSearchDto;
import com.example.phantom_mask.dto.StoreSearchDto;
import com.example.phantom_mask.service.search.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/1.0/search")
public class SearchController {

    private final SearchService searchService;


    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Search endpoint for masks and stores.
     *
     * @param type   The type of search, either "mask" or "store".
     * @param q      The search query.
     * @param limit  The maximum number of results to return.
     * @param offset The offset for pagination.
     * @return A ResponseEntity containing the search results or an error message.
     */

    @GetMapping
    public ResponseEntity<?> search(
        @RequestParam(value = "type", defaultValue = "mask") String type,
        @RequestParam("q") String q,
        @RequestParam(value = "limit", defaultValue = "20") int limit,
        @RequestParam(value = "offset", defaultValue = "0") int offset
    ) {
        try {
            if("store".equalsIgnoreCase(type)) {
                List<StoreSearchDto> stores = searchService.searchStoresByName(q, limit, offset);
                return ResponseEntity.ok(Map.of("data", stores));

            } else if("mask".equalsIgnoreCase(type)) {
                List<MaskSearchDto> masks = searchService.searchMasksByName(q, limit, offset);
                return ResponseEntity.ok(Map.of("data", masks));
            } else {
                throw new IllegalArgumentException("Invalid type. Allowed values: store, mask.");
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
