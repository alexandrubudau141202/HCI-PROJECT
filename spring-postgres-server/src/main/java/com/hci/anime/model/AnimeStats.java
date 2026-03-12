package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "anime_stats")
public class AnimeStats {

    @Id
    @Column(name = "mal_id")
    private Integer malId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "mal_id")
    @JsonIgnore
    private Anime anime;

    @Column(name = "watching")
    private Integer watching;

    @Column(name = "completed")
    private Integer completed;

    @Column(name = "on_hold")
    private Integer onHold;

    @Column(name = "dropped")
    private Integer dropped;

    @Column(name = "plan_to_watch")
    private Integer planToWatch;

    @Column(name = "total")
    private Integer total;

    @Column(name = "score_1_votes")  private Integer score1Votes;
    @Column(name = "score_2_votes")  private Integer score2Votes;
    @Column(name = "score_3_votes")  private Integer score3Votes;
    @Column(name = "score_4_votes")  private Integer score4Votes;
    @Column(name = "score_5_votes")  private Integer score5Votes;
    @Column(name = "score_6_votes")  private Integer score6Votes;
    @Column(name = "score_7_votes")  private Integer score7Votes;
    @Column(name = "score_8_votes")  private Integer score8Votes;
    @Column(name = "score_9_votes")  private Integer score9Votes;
    @Column(name = "score_10_votes") private Integer score10Votes;
}