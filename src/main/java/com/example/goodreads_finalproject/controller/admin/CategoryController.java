package com.example.goodreads_finalproject.controller.admin;

import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.model.request.CategoryRequest;
import com.example.goodreads_finalproject.model.response.CategoryResponse;
import com.example.goodreads_finalproject.model.response.CommonResponse;
import com.example.goodreads_finalproject.service.BookService;
import com.example.goodreads_finalproject.service.CategoryService;
import com.example.goodreads_finalproject.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping()
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    BookService bookService;
    UserService userService;
    CategoryService categoryService;

    @GetMapping("/admin/categories")
    public String allBook(Model model, CategoryRequest request) {
        CommonResponse<?> commonResponse = categoryService.searchCategory(request);

        model.addAttribute("pageCategoryInfo", commonResponse);
        model.addAttribute("currentPage", request.getPageIndex());
        model.addAttribute("pageSize", request.getPageSize());

        return "admin/category/category-list";
    }

    // API
    @GetMapping("/api/v1/admin/category/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
        CategoryResponse categoryResponse = categoryService.findCategory(categoryId);
        return ResponseEntity.ok(categoryResponse);
    }

    @PostMapping("/api/v1/admin/category")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest newCategoryRequest) {
        try {
            categoryService.createCategory(newCategoryRequest);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("Category existed", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/v1/admin/category/{categoryId}")
    public ResponseEntity<?> editCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest) {
        categoryService.editCategory(categoryId, categoryRequest);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/admin/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) throws BadRequestException {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
