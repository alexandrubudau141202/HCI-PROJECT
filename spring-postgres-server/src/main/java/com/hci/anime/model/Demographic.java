package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "demographics")
public class Demographic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "demographic_id")
    private Integer demographicId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "demographics")
    @JsonIgnore
    private Set<Anime> animes;
}