package com.hci.anime.controller;

import com.hci.anime.model.User;
import com.hci.anime.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<User> getById(@PathVariable String username) {
        return userService.findById(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<Page<User>> getByLocation(
            @PathVariable String location,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.findByLocation(location, page, size));
    }

    @GetMapping("/joined/{year}")
    public ResponseEntity<Page<User>> getByJoinedYear(
            @PathVariable Integer year,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.findByJoinedYear(year, page, size));
    }
}