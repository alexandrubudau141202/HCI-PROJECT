package com.hci.anime.service;

import com.hci.anime.model.Person;
import com.hci.anime.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Optional<Person> findById(Integer personMalId) {
        return personRepository.findById(personMalId);
    }

    public Page<Person> searchByName(String name, int page, int size) {
        return personRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, size));
    }

    public Page<Person> findTopByFavorites(int page, int size) {
        return personRepository.findTopByFavorites(PageRequest.of(page, size));
    }
}