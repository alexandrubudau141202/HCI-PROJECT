package com.hci.anime.controller;

import com.hci.anime.model.Anime;
import com.hci.anime.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anime")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findAll(page, size));
    }

    @GetMapping("/{malId}")
    public ResponseEntity<Anime> getById(@PathVariable Integer malId) {
        return animeService.findById(malId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Anime>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.searchByTitle(q, page, size));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<Anime>> getByType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findByType(type, page, size));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Page<Anime>> getByYear(
            @PathVariable Integer year,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findByYear(year, page, size));
    }

    @GetMapping("/season")
    public ResponseEntity<Page<Anime>> getBySeason(
            @RequestParam String season,
            @RequestParam Integer year,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findBySeasonAndYear(season, year, page, size));
    }

    @GetMapping("/top/score")
    public ResponseEntity<Page<Anime>> getTopByScore(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findTopByScore(page, size));
    }

    @GetMapping("/top/popularity")
    public ResponseEntity<Page<Anime>> getTopByPopularity(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findTopByPopularity(page, size));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<Anime>> getByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findByGenre(genre, page, size));
    }

    @GetMapping("/studio/{studio}")
    public ResponseEntity<Page<Anime>> getByStudio(
            @PathVariable String studio,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(animeService.findByStudio(studio, page, size));
    }
}