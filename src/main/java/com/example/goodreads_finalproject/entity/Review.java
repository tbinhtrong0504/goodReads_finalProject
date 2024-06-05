package com.example.goodreads_finalproject.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @Column(nullable = false, columnDefinition = "default 0")
    double rating;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    LocalDateTime deletedDateTime;
}
