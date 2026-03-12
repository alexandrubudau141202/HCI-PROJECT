package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "character_anime")
@IdClass(CharacterAnime.CharacterAnimeId.class)
public class CharacterAnime {

    @Getter
    @Setter
    public static class CharacterAnimeId implements Serializable {
        private Integer animeMalId;
        private Integer characterMalId;
    }

    @Id
    @Column(name = "anime_mal_id")
    private Integer animeMalId;

    @Id
    @Column(name = "character_mal_id")
    private Integer characterMalId;

    @Column(name = "character_name")
    private String characterName;

    @Column(name = "role")
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Anime anime;
}