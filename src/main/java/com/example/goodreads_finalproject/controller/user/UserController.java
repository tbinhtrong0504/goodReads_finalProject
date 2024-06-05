package com.example.goodreads_finalproject.controller.user;

import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.exception.NotFoundException;
import com.example.goodreads_finalproject.model.request.*;
import com.example.goodreads_finalproject.model.response.BookResponse;
import com.example.goodreads_finalproject.model.response.ChallengeResponse;
import com.example.goodreads_finalproject.model.response.CommentResponse;
import com.example.goodreads_finalproject.model.response.LocationResponse;
import com.example.goodreads_finalproject.security.SecurityUtils;
import com.example.goodreads_finalproject.service.BookService;
import com.example.goodreads_finalproject.service.ChallengeService;
import com.example.goodreads_finalproject.service.FollowingService;
import com.example.goodreads_finalproject.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    BookService bookService;
    FollowingService followingService;
    ChallengeService challengeService;

    // Thymleaf
    @GetMapping("/users/my-book")
    public String getMyBookPage(Model model) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();

        model.addAttribute("myBookList", bookService.getMyBookPagination(ReadingBookRequest.builder()
                .userId(userIdOptional.get())
                .build()));
        model.addAttribute("countMyBookList", bookService.countMyBookList(userIdOptional.get()));
        return "user/my-book";
    }

    @GetMapping("/users/review/{bookId}")
    public String getReviewPage(Model model, @PathVariable Long bookId) {
        Optional<Long> optionalId = SecurityUtils.getCurrentUserLoginId();
        BookResponse bookResponse = bookService.findBookByBookId(bookId, optionalId.get());
        model.addAttribute("bookDetail", bookResponse);

        return "user/review-detail";
    }

    @GetMapping("/users/challenge")
    public String getChallengePage(Model model) {
        Optional<Long> optionalId = SecurityUtils.getCurrentUserLoginId();
        ChallengeResponse challengeResponse = challengeService.findChallengeByUserId(optionalId.get());
        model.addAttribute("challengeDetail", challengeResponse);

        return "user/reading-challenge";
    }


    // API

    @PostMapping("/api/v1/users/book-reading")
    public ResponseEntity<?> markBook(@RequestBody ReadingBookRequest request) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        request.setUserId(userIdOptional.get());
        bookService.markBook(request);
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/users/book-reading/{bookId}")
    public ResponseEntity<?> removeMarkBook(@PathVariable Long bookId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.removeMarkBook(bookId, userIdOptional.get());
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    // Rating
    @PostMapping("/api/v1/users/rating")
    public ResponseEntity<?> changeRating(@RequestBody @Valid ReviewRequest request) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.changeRating(request, userIdOptional.get());
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/users/rating/{bookId}")
    public ResponseEntity<?> removeRating(@PathVariable Long bookId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.removeRating(bookId, userIdOptional.get());
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    // Review
    @PostMapping("/api/v1/users/review")
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest request) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.saveReview(request, userIdOptional.get());
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    @PutMapping("/api/v1/users/review")
    public ResponseEntity<?> updateReview(@RequestBody @Valid ReviewRequest request) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.saveReview(request, userIdOptional.get());
        return new ResponseEntity<>("Successful", HttpStatus.OK);
    }

//    @DeleteMapping("/api/v1/users/review/{reviewId}")
//    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
//        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
//        if (userIdOptional.isEmpty()) {
//            throw new NotFoundException("not found user");
//        }
//        bookService.deleteReview(reviewId);
//        return new ResponseEntity<>("Successful", HttpStatus.OK);
//    }

    // Like
    @PostMapping("/api/v1/users/like/{reviewId}")
    public ResponseEntity<?> likeReview(@PathVariable Long reviewId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.likeReview(reviewId, userIdOptional.get());
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/users/like/{reviewId}")
    public ResponseEntity<?> unLikeReview(@PathVariable Long reviewId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.unLikeReview(reviewId, userIdOptional.get());
        return new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    // Comment
    @PostMapping("/api/v1/users/comment")
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequest request) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        CommentResponse commentResponse = bookService.createComment(request, userIdOptional.get());
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/users/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        bookService.deleteComment(commentId);
        return new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    // Following
    @PostMapping("/api/v1/users/following/{userAcceptId}")
    public ResponseEntity<?> followUser(@PathVariable Long userAcceptId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        followingService.followUser(userIdOptional.get(), userAcceptId);
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/v1/users/following/{userAcceptId}")
    public ResponseEntity<?> unFollow(@PathVariable Long userAcceptId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        followingService.unFollow(userIdOptional.get(), userAcceptId);
        return new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    // Reading Challenge
    @PostMapping("/api/v1/users/reading-challenge")
    public ResponseEntity<?> createChallenge(@RequestBody @Valid ChallengeRequest challengeRequest) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        challengeService.createChallenge(userIdOptional.get(), challengeRequest);
        return new ResponseEntity<>("Successful", HttpStatus.CREATED);
    }

    @PutMapping("/api/v1/users/reading-challenge/{challengeId}")
    public ResponseEntity<?> updateChallenge(@RequestBody @Valid ChallengeRequest challengeRequest, @PathVariable Long challengeId) {
        challengeService.updateChallenge(challengeRequest, challengeId);
        return new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/users/reading-challenge/{challengeId}")
    public ResponseEntity<?> deleteChallenge(@PathVariable Long challengeId) {
        Optional<Long> userIdOptional = SecurityUtils.getCurrentUserLoginId();
        if (userIdOptional.isEmpty()) {
            throw new NotFoundException("not found user");
        }
        challengeService.deleteChallenge(challengeId);
        return new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    // Address
    @GetMapping("/api/v1/users/districts/{provinceCode}")
    public ResponseEntity<?> getDistricts(@PathVariable String provinceCode) {
        try {
            List<LocationResponse> districts = userService.getDistricts(provinceCode);
            return new ResponseEntity<>(districts, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/v1/users/wards/{districtCode}")
    public ResponseEntity<?> getWards(@PathVariable String districtCode) {
        try {
            List<LocationResponse> wards = userService.getWards(districtCode);
            return new ResponseEntity<>(wards, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/v1/users/address")
    public ResponseEntity<?> updateAddress(@RequestBody WardRequest wardRequest) {
        userService.updateAddress(wardRequest);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
