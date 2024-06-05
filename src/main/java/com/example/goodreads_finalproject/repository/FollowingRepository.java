package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Following;
import com.example.goodreads_finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    Optional<Following> findByUserRequestAndUserAccept(User userRequest, User userAccept);

}