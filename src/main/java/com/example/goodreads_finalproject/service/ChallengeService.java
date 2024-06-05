package com.example.goodreads_finalproject.service;

import com.example.goodreads_finalproject.entity.*;
import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.exception.NotFoundException;
import com.example.goodreads_finalproject.model.request.*;
import com.example.goodreads_finalproject.model.response.*;
import com.example.goodreads_finalproject.repository.*;
import com.example.goodreads_finalproject.repository.custom.BookCustomRepository;
import com.example.goodreads_finalproject.repository.custom.ChallengeCustomRepository;
import com.example.goodreads_finalproject.repository.custom.CommentCustomRepository;
import com.example.goodreads_finalproject.repository.custom.ReviewCustomRepository;
import com.example.goodreads_finalproject.statics.ChallengeStatus;
import com.example.goodreads_finalproject.statics.ReadingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeService {
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
    LikeRepository likeRepository;
    ChallengeRepository challengeRepository;
    ChallengeCustomRepository challengeCustomRepository;


    public ChallengeResponse findChallengeByUserId(Long userId) {
        return challengeCustomRepository.getChallengeDetail(userId);
    }

    public void createChallenge(Long userId, ChallengeRequest challengeRequest) {
        User user = userRepository.findById(userId).get();
        Challenge challenge = Challenge.builder()
                .user(user)
                .numberOfBook(challengeRequest.getNumberOfBook())
                .challengeStatus(ChallengeStatus.HAPPENING)
                .startedDate(challengeRequest.getStartedDate())
                .endDate(challengeRequest.getEndDate())
                .build();
        challengeRepository.save(challenge);
    }

    public void updateChallenge(ChallengeRequest challengeRequest, Long challengeId) {
        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);
        if (challengeOptional.isEmpty()) {
            throw new NotFoundException("Not found challenge");
        }
        Challenge challenge = challengeOptional.get();
        challenge.setNumberOfBook(challengeRequest.getNumberOfBook());
        challenge.setEndDate(challengeRequest.getEndDate());
        challengeRepository.save(challenge);
    }

    public void deleteChallenge(Long challengeId) {
        challengeRepository.deleteById(challengeId);
    }
}

