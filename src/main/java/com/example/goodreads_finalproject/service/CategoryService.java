package com.example.goodreads_finalproject.service;

import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.Category;
import com.example.goodreads_finalproject.entity.ReadingBook;
import com.example.goodreads_finalproject.entity.User;
import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.exception.NotFoundException;
import com.example.goodreads_finalproject.model.request.*;
import com.example.goodreads_finalproject.model.response.BookResponse;
import com.example.goodreads_finalproject.model.response.CategoryResponse;
import com.example.goodreads_finalproject.model.response.CommonResponse;
import com.example.goodreads_finalproject.model.response.ReadingBookResponse;
import com.example.goodreads_finalproject.repository.*;
import com.example.goodreads_finalproject.repository.custom.BookCustomRepository;
import com.example.goodreads_finalproject.statics.ReadingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    BookRepository bookRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    ReadingBookRepository readingBookRepository;
    BookOfChallengeRepository bookOfChallengeRepository;
    ObjectMapper objectMapper;
    BookCustomRepository bookCustomRepository;

    //category

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<CategoryResponse> getAllCategories() {
        List<CategoryResponse> result = new ArrayList<>();
        categoryRepository.findAll().forEach(category -> result.add(objectMapper.convertValue(category, CategoryResponse.class)));
        return result;
    }

    public void createCategory(CategoryRequest newCategoryRequest) throws BadRequestException {
        Optional<Category> categoryOptional = categoryRepository.findAllByName(newCategoryRequest.getName());
        if (categoryOptional.isPresent()) {
            throw new BadRequestException();
        }
        Category category = Category.builder()
                .name(newCategoryRequest.getName())
                .build();
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) throws BadRequestException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Not found category");
        }
        List<Book> bookList = bookRepository.findAllByCategories(categoryOptional.get());
        if (bookList.size() > 0) {
            throw new BadRequestException();
        }
        categoryRepository.deleteById(id);
    }


    public Page<Book> getAllBook(Integer page, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(page - 1, pageSize);
        return bookRepository.findAll(pageRequest);
    }

    public BookResponse findBookByBookId(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new NotFoundException("Book not found!");
        }
        Book book = bookOptional.get();

        return objectMapper.convertValue(book, BookResponse.class);
    }

    public CommonResponse<?> searchCategory(CategoryRequest request) {
        try {
            List<Category> categories;
            if (request.getName() == null) {
                categories = categoryRepository.findAll();
            } else {
                categories = categoryRepository.findAllByNameContainingIgnoreCase(request.getName());
            }
            List<CategoryResponse> categoryResponses = new ArrayList<>();
            for (Category category : categories) {
                categoryResponses.add(objectMapper.convertValue(category, CategoryResponse.class));
            }
            Integer pageIndex = request.getPageIndex();
            Integer pageSize = request.getPageSize();

            PaginationUtils<CategoryResponse> paginationUtils = new PaginationUtils<>();
            int pageNumber = paginationUtils.getPageNumber(categoryResponses, pageSize);
            categoryResponses = paginationUtils.searchData(categoryResponses, pageIndex, pageSize);

            return CommonResponse.builder()
                    .pageNumber(pageNumber)
                    .data(categoryResponses)
                    .build();
        } catch (NotFoundException e) {
            throw new NotFoundException("Page index out of bound!");
        }
    }

    private Category findCategoryById(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        return categoryOptional.get();
    }

    public CategoryResponse findCategory(Long categoryId) {
        return objectMapper.convertValue(findCategoryById(categoryId), CategoryResponse.class);
    }

    public void editCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = findCategoryById(categoryId);
        category.setName(categoryRequest.getName());
        categoryRepository.save(category);
    }
}

