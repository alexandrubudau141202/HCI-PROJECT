package com.hci.anime.controller;

import com.hci.anime.model.Character;
import com.hci.anime.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/{characterMalId}")
    public ResponseEntity<Character> getById(@PathVariable Integer characterMalId) {
        return characterService.findById(characterMalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Character>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(characterService.searchByName(q, page, size));
    }

    @GetMapping("/top")
    public ResponseEntity<Page<Character>> getTopByFavorites(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(characterService.findTopByFavorites(page, size));
    }

    @GetMapping("/anime/{animeId}")
    public ResponseEntity<Page<Character>> getByAnime(
            @PathVariable Integer animeId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(characterService.findByAnimeId(animeId, page, size));
    }
}