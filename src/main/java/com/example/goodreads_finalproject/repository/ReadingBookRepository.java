package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.ReadingBook;
import com.example.goodreads_finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingBookRepository extends JpaRepository<ReadingBook, Long> {

//    Page<ReadingBook> findAllByUser(User user, Pageable pageable);


    List<ReadingBook> findAllByUser(User user);

    Optional<ReadingBook> findAllByBook(Book book);

    Optional<ReadingBook> findByUserAndBook(User user, Book book);
}