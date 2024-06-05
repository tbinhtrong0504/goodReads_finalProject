package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.Category;
import com.example.goodreads_finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategories(Category category);

    Page<Book> findAllByTitleOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

    Page<Book> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findAllByAuthorContainingIgnoreCase(String author, Pageable pageable);

    @Query("select b from Book b where lower(b.title) like :title and lower(b.author) like :author")
    Page<Book> findAllByAuthorContainingIgnoreCase1(String title, String author, Pageable pageable);

}