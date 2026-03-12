package com.hci.anime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "location")
    private String location;

    @Column(name = "joined")
    private LocalDate joined;

    @Column(name = "joined_year")
    private Integer joinedYear;
}