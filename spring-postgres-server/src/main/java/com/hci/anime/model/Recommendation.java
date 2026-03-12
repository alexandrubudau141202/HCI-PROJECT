package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "recommendations")
@IdClass(Recommendation.RecommendationId.class)
public class Recommendation {

    @Getter
    @Setter
    public static class RecommendationId implements Serializable {
        private Integer malId;
        private Integer recommendationMalId;
    }

    @Id
    @Column(name = "mal_id")
    private Integer malId;

    @Id
    @Column(name = "recommendation_mal_id")
    private Integer recommendationMalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Anime anime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Anime recommendedAnime;
}