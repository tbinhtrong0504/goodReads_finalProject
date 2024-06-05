package com.example.goodreads_finalproject.model.response;

import com.example.goodreads_finalproject.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookResponse {

    Long id;

    String image;

    String title;

    Set<Category> categories;

    String author;

    String description;

    double rating;

    double ratingDetail;

    Integer pages;

    LocalDate published;

    String buyBook;

    String readingStatus;

    Integer countOfRatings;

    Integer countOfReviews;

    String content;
}
