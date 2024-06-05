package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    Long bookId;

    String image;

    @NotBlank
    @Size(max = 100, message = "Title cannot over 100 characters")
    String title;

    @NotEmpty
    Set<Long> categoryId;

    @NotBlank
    @Size(max = 100, message = "Author cannot over 100 characters")
    String author;

    @Size(max = 65535, message = "Description cannot over 65.535 characters")
    String description;

    @NotNull
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @Past
    LocalDate published;

    String buyBook;

    double rating;

    @NotNull
    @Max(value = 21450)
    Integer pages;
}
