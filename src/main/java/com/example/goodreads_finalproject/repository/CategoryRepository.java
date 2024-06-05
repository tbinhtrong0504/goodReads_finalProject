package com.example.goodreads_finalproject.repository;


import com.example.goodreads_finalproject.entity.Category;
import com.example.goodreads_finalproject.entity.User;
import com.example.goodreads_finalproject.model.request.CategoryRequest;
import com.example.goodreads_finalproject.model.response.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    List<Category> findAllByNameContainingIgnoreCase(String nameCategory);

    Optional<Category> findAllByName(String nameCategory);
}