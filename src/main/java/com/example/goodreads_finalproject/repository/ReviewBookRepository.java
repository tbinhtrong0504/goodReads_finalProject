package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.ReadingBook;
import com.example.goodreads_finalproject.entity.Review;
import com.example.goodreads_finalproject.entity.User;
import com.example.goodreads_finalproject.model.response.AvgRatingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewBookRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndBook(User user, Book book);


}