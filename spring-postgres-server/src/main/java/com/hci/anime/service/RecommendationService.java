package com.hci.anime.service;

import com.hci.anime.model.Anime;
import com.hci.anime.model.Recommendation;
import com.hci.anime.repository.AnimeRepository;
import com.hci.anime.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final AnimeRepository           animeRepository;

    public List<Recommendation> findByAnimeId(Integer malId) {
        return recommendationRepository.findByMalId(malId);
    }

    public List<Map<String, Object>> findTopRecommended(Integer limit) {
        List<Object[]> raw = recommendationRepository.findTopRecommended(limit);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : raw) {
            Integer animeId  = ((Number) row[0]).intValue();
            Long    recCount = ((Number) row[1]).longValue();

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("recCount", recCount);
            animeRepository.findById(animeId).ifPresent(a -> {
                entry.put("malId", a.getMalId());
                entry.put("title", a.getTitle());
                entry.put("score", a.getScore());
                entry.put("type",  a.getType());
            });
            result.add(entry);
        }
        return result;
    }
}