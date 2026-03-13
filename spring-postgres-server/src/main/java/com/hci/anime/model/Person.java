package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "people")
public class Person {

    @Id
    @Column(name = "person_mal_id")
    private Integer personMalId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "relevant_location")
    private String relevantLocation;

    @Column(name = "favorites")
    private Integer favorites;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PersonAlternateName> alternateNames;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PersonAnimeWork> animeWorks;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<PersonVoiceWork> voiceWorks;
}