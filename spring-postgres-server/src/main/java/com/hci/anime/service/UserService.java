package com.hci.anime.service;

import com.hci.anime.model.User;
import com.hci.anime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(String username) {
        return userRepository.findById(username);
    }

    public Page<User> findAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public Page<User> findByLocation(String location, int page, int size) {
        return userRepository.findByLocation(location, PageRequest.of(page, size));
    }

    public Page<User> findByJoinedYear(Integer year, int page, int size) {
        return userRepository.findByJoinedYear(year, PageRequest.of(page, size));
    }
}