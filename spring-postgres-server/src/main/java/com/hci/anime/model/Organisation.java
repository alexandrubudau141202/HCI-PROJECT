package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "organisations")
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    private Integer orgId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "org_type")
    private String orgType;

    @ManyToMany(mappedBy = "organisations")
    @JsonIgnore
    private Set<Anime> animes;
}