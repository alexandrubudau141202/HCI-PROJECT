package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "character_nicknames")
@IdClass(CharacterNickname.CharacterNicknameId.class)
public class CharacterNickname {

    @Getter
    @Setter
    public static class CharacterNicknameId implements Serializable {
        private Integer characterMalId;
        private String nickname;
    }

    @Id
    @Column(name = "character_mal_id")
    private Integer characterMalId;

    @Id
    @Column(name = "nickname")
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Character character;
}