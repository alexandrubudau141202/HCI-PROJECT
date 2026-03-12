package com.hci.anime.repository;

import com.hci.anime.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findByLocation(String location, Pageable pageable);

    Page<User> findByJoinedYear(Integer joinedYear, Pageable pageable);
}