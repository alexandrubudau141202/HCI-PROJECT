package com.hci.anime.repository;

import com.hci.anime.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Recommendation.RecommendationId> {

    @Query("SELECT r FROM Recommendation r WHERE r.malId = :malId")
    List<Recommendation> findByMalId(@Param("malId") Integer malId);

    @Query(value = """
        SELECT r.recommendation_mal_id, COUNT(*) as rec_count
        FROM recommendations r
        GROUP BY r.recommendation_mal_id
        ORDER BY rec_count DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> findTopRecommended(@Param("limit") Integer limit);
}