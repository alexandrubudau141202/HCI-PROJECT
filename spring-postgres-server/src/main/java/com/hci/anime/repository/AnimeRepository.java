package com.hci.anime.repository;

import com.hci.anime.model.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Anime, Integer> {

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    Optional<Anime> findById(Integer malId);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    Page<Anime> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    Page<Anime> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    Page<Anime> findByType(String type, Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    Page<Anime> findByYear(Integer year, Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    Page<Anime> findBySeasonAndYear(String season, Integer year, Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    @Query("SELECT DISTINCT a FROM Anime a WHERE a.score IS NOT NULL ORDER BY a.score DESC")
    Page<Anime> findTopByScore(Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    @Query("SELECT DISTINCT a FROM Anime a WHERE a.popularity IS NOT NULL ORDER BY a.popularity ASC")
    Page<Anime> findTopByPopularity(Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    @Query("SELECT DISTINCT a FROM Anime a JOIN a.genres g WHERE g.name = :genre")
    Page<Anime> findByGenreName(@Param("genre") String genre, Pageable pageable);

    @EntityGraph(attributePaths = {"genres", "themes", "demographics", "organisations", "stats"})
    @Query("SELECT DISTINCT a FROM Anime a JOIN a.organisations o WHERE o.name = :studio AND o.orgType = 'studio'")
    Page<Anime> findByStudio(@Param("studio") String studio, Pageable pageable);
}