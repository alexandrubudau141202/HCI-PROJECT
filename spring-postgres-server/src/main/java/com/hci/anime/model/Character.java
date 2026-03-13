package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "characters")
public class Character {

    @Id
    @Column(name = "character_mal_id")
    private Integer characterMalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "name_kanji")
    private String nameKanji;

    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    @Column(name = "favorites")
    private Integer favorites;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CharacterNickname> nicknames;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CharacterAnime> animeAppearances;
}