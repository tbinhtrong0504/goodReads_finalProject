package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.BookOfChallenge;
import com.example.goodreads_finalproject.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, String> {

}