package com.example.goodreads_finalproject.service;

import com.example.goodreads_finalproject.entity.*;
import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.exception.NotFoundException;
import com.example.goodreads_finalproject.model.request.*;
import com.example.goodreads_finalproject.model.response.*;
import com.example.goodreads_finalproject.repository.*;
import com.example.goodreads_finalproject.repository.custom.BookCustomRepository;
import com.example.goodreads_finalproject.repository.custom.CommentCustomRepository;
import com.example.goodreads_finalproject.repository.custom.ReviewCustomRepository;
import com.example.goodreads_finalproject.statics.FollowingStatus;
import com.example.goodreads_finalproject.statics.ReadingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowingService {
    BookRepository bookRepository;
    UserRepository userRepository;
    CategoryService categoryService;
    ReadingBookRepository readingBookRepository;
    BookOfChallengeRepository bookOfChallengeRepository;
    ObjectMapper objectMapper;
    BookCustomRepository bookCustomRepository;
    ReviewBookRepository reviewBookRepository;
    ReviewCustomRepository reviewCustomRepository;
    CommentCustomRepository commentCustomRepository;
    CommentRepository commentRepository;
    FollowingRepository followingRepository;

    public void followUser(Long userRequestId, Long userAcceptId) {
        User userRequest = userRepository.findById(userRequestId).get();
        User userAccept = userRepository.findById(userAcceptId).get();
        Following following = Following.builder()
                .userRequest(userRequest)
                .userAccept(userAccept)
                .followingStatus(FollowingStatus.FOLLOWING)
                .acceptedDateTime(LocalDateTime.now())
                .build();
        followingRepository.save(following);
    }

    public void unFollow(Long userRequestId, Long userAcceptId) {
        User userRequest = userRepository.findById(userRequestId).get();
        User userAccept = userRepository.findById(userAcceptId).get();
        Optional<Following> followingOptional = followingRepository.findByUserRequestAndUserAccept(userRequest, userAccept);
        if (followingOptional.isEmpty()) {
            throw new NotFoundException("Not following");
        }
        followingRepository.delete(followingOptional.get());
    }
}

