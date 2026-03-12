package com.hci.anime.controller;

import com.hci.anime.model.Recommendation;
import com.hci.anime.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{malId}")
    public ResponseEntity<List<Recommendation>> getByAnimeId(@PathVariable Integer malId) {
        return ResponseEntity.ok(recommendationService.findByAnimeId(malId));
    }

    @GetMapping("/top")
    public ResponseEntity<List<Map<String, Object>>> getTopRecommended(
            @RequestParam(defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(recommendationService.findTopRecommended(limit));
    }
}