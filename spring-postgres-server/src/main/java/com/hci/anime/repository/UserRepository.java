package com.hci.anime.repository;

import com.hci.anime.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE LOWER(u.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    Page<User> findByLocation(@Param("location") String location, Pageable pageable);

    Page<User> findByJoinedYear(Integer joinedYear, Pageable pageable);
}