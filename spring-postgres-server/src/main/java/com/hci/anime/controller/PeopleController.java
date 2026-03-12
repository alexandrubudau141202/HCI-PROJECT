package com.hci.anime.controller;

import com.hci.anime.model.Person;
import com.hci.anime.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PeopleController {

    private final PersonService personService;

    @GetMapping("/{personMalId}")
    public ResponseEntity<Person> getById(@PathVariable Integer personMalId) {
        return personService.findById(personMalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Person>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(personService.searchByName(q, page, size));
    }

    @GetMapping("/top")
    public ResponseEntity<Page<Person>> getTopByFavorites(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(personService.findTopByFavorites(page, size));
    }
}