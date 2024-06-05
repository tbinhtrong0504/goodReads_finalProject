package com.example.goodreads_finalproject.service;

import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.Category;
import com.example.goodreads_finalproject.entity.Role;
import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.exception.NotFoundException;
import com.example.goodreads_finalproject.model.request.CategoryRequest;
import com.example.goodreads_finalproject.model.request.RoleRequest;
import com.example.goodreads_finalproject.model.response.BookResponse;
import com.example.goodreads_finalproject.model.response.CategoryResponse;
import com.example.goodreads_finalproject.model.response.CommonResponse;
import com.example.goodreads_finalproject.repository.*;
import com.example.goodreads_finalproject.repository.custom.BookCustomRepository;
import com.example.goodreads_finalproject.statics.Roles;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    BookRepository bookRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    ReadingBookRepository readingBookRepository;
    BookOfChallengeRepository bookOfChallengeRepository;
    ObjectMapper objectMapper;
    BookCustomRepository bookCustomRepository;
    RoleRepository roleRepository;

    public List<Role> findAllRole() {
        List<Role> result = roleRepository.findAll();
        return result;
    }

    public void createRole(RoleRequest request) throws BadRequestException {
        Optional<Role> roleOptional = roleRepository.findByName(Roles.valueOf(request.getName()));
        if (roleOptional.isPresent()) {
            throw new BadRequestException();
        }
        Role role = Role.builder()
                .name(Roles.valueOf(request.getName()))
                .build();
        roleRepository.save(role);
    }
}

