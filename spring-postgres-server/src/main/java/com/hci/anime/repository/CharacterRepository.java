package com.hci.anime.repository;

import com.hci.anime.model.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CharacterRepository extends JpaRepository<Character, Integer> {

    Page<Character> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM Character c ORDER BY c.favorites DESC")
    Page<Character> findTopByFavorites(Pageable pageable);

    @Query("SELECT c FROM Character c JOIN c.animeAppearances a WHERE a.animeMalId = :animeId")
    Page<Character> findByAnimeId(@Param("animeId") Integer animeId, Pageable pageable);
}