package com.hci.anime.repository;

import com.hci.anime.model.AnimeStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeStatsRepository extends JpaRepository<AnimeStats, Integer> {
}