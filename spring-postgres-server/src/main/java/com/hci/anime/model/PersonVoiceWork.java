package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "person_voice_works")
@IdClass(PersonVoiceWork.PersonVoiceWorkId.class)
public class PersonVoiceWork {

    @Getter
    @Setter
    public static class PersonVoiceWorkId implements Serializable {
        private Integer personMalId;
        private Integer animeMalId;
        private Integer characterMalId;
    }

    @Id
    @Column(name = "person_mal_id")
    private Integer personMalId;

    @Id
    @Column(name = "anime_mal_id")
    private Integer animeMalId;

    @Id
    @Column(name = "character_mal_id")
    private Integer characterMalId;

    @Column(name = "role")
    private String role;

    @Column(name = "language")
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Anime anime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Character character;
}