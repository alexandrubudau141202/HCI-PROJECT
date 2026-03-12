package com.hci.anime.repository;

import com.hci.anime.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Page<Person> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM Person p ORDER BY p.favorites DESC")
    Page<Person> findTopByFavorites(Pageable pageable);
}