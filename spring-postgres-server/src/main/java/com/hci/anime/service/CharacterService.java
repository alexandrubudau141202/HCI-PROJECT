package com.hci.anime.service;

import com.hci.anime.model.Character;
import com.hci.anime.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;

    public Optional<Character> findById(Integer characterMalId) {
        return characterRepository.findById(characterMalId);
    }

    public Page<Character> searchByName(String name, int page, int size) {
        return characterRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, size));
    }

    public Page<Character> findTopByFavorites(int page, int size) {
        return characterRepository.findTopByFavorites(PageRequest.of(page, size));
    }

    public Page<Character> findByAnimeId(Integer animeId, int page, int size) {
        return characterRepository.findByAnimeId(animeId, PageRequest.of(page, size));
    }
}