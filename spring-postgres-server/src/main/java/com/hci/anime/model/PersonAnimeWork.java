package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "person_anime_works")
@IdClass(PersonAnimeWork.PersonAnimeWorkId.class)
public class PersonAnimeWork {

    @Getter
    @Setter
    public static class PersonAnimeWorkId implements Serializable {
        private Integer personMalId;
        private Integer animeMalId;
    }

    @Id
    @Column(name = "person_mal_id")
    private Integer personMalId;

    @Id
    @Column(name = "anime_mal_id")
    private Integer animeMalId;

    @Column(name = "position")
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Anime anime;
}