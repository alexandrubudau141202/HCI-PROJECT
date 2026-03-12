package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "person_alternate_names")
@IdClass(PersonAlternateName.PersonAlternateNameId.class)
public class PersonAlternateName {

    @Getter
    @Setter
    public static class PersonAlternateNameId implements Serializable {
        private Integer personMalId;
        private String altName;
    }

    @Id
    @Column(name = "person_mal_id")
    private Integer personMalId;

    @Id
    @Column(name = "alt_name")
    private String altName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_mal_id", insertable = false, updatable = false)
    @JsonIgnore
    private Person person;
}