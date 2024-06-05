package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Challenge;
import com.example.goodreads_finalproject.entity.Like;
import com.example.goodreads_finalproject.entity.Review;
import com.example.goodreads_finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}