package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.BookOfChallenge;
import com.example.goodreads_finalproject.entity.ReadingBook;
import com.example.goodreads_finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookOfChallengeRepository extends JpaRepository<BookOfChallenge, Long> {


    Optional<BookOfChallenge> findAllByBook(Book book);
}