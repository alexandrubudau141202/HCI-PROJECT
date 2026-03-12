package com.hci.anime.service;

import com.hci.anime.model.Anime;
import com.hci.anime.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Optional<Anime> findById(Integer malId) {
        return animeRepository.findById(malId);
    }

    public Page<Anime> findAll(int page, int size) {
        return animeRepository.findAll(PageRequest.of(page, size, Sort.by("malId")));
    }

    public Page<Anime> searchByTitle(String title, int page, int size) {
        return animeRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    public Page<Anime> findByType(String type, int page, int size) {
        return animeRepository.findByType(type, PageRequest.of(page, size));
    }

    public Page<Anime> findByYear(Integer year, int page, int size) {
        return animeRepository.findByYear(year, PageRequest.of(page, size));
    }

    public Page<Anime> findBySeasonAndYear(String season, Integer year, int page, int size) {
        return animeRepository.findBySeasonAndYear(season, year, PageRequest.of(page, size));
    }

    public Page<Anime> findTopByScore(int page, int size) {
        return animeRepository.findTopByScore(PageRequest.of(page, size));
    }

    public Page<Anime> findTopByPopularity(int page, int size) {
        return animeRepository.findTopByPopularity(PageRequest.of(page, size));
    }

    public Page<Anime> findByGenre(String genre, int page, int size) {
        return animeRepository.findByGenreName(genre, PageRequest.of(page, size));
    }

    public Page<Anime> findByStudio(String studio, int page, int size) {
        return animeRepository.findByStudio(studio, PageRequest.of(page, size));
    }
}