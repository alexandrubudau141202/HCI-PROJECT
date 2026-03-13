package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "anime")
public class Anime {

    @Id
    @Column(name = "mal_id")
    private Integer malId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "title_japanese")
    private String titleJapanese;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "source")
    private String source;

    @Column(name = "rating")
    private String rating;

    @Column(name = "episodes")
    private Integer episodes;

    @Column(name = "season")
    private String season;

    @Column(name = "year")
    private Integer year;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "synopsis", columnDefinition = "TEXT")
    private String synopsis;

    @Column(name = "score", precision = 4, scale = 2)
    private BigDecimal score;

    @Column(name = "scored_by")
    private Integer scoredBy;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "popularity")
    private Integer popularity;

    @Column(name = "members")
    private Integer members;

    @Column(name = "favorites")
    private Integer favorites;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToOne(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"anime", "hibernateLazyInitializer", "handler"})
    private AnimeStats stats;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "anime_genres",
        joinColumns = @JoinColumn(name = "mal_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnoreProperties({"animes", "hibernateLazyInitializer", "handler"})
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "anime_themes",
        joinColumns = @JoinColumn(name = "mal_id"),
        inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    @JsonIgnoreProperties({"animes", "hibernateLazyInitializer", "handler"})
    private Set<Theme> themes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "anime_demographics",
        joinColumns = @JoinColumn(name = "mal_id"),
        inverseJoinColumns = @JoinColumn(name = "demographic_id")
    )
    @JsonIgnoreProperties({"animes", "hibernateLazyInitializer", "handler"})
    private Set<Demographic> demographics;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "anime_organisations",
        joinColumns = @JoinColumn(name = "mal_id"),
        inverseJoinColumns = @JoinColumn(name = "org_id")
    )
    @JsonIgnoreProperties({"animes", "hibernateLazyInitializer", "handler"})
    private Set<Organisation> organisations;
}