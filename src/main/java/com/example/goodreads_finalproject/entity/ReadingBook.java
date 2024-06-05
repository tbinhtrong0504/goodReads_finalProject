package com.example.goodreads_finalproject.entity;

import com.example.goodreads_finalproject.statics.ReadingStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadingBook extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @Enumerated(EnumType.STRING)
    ReadingStatus readingStatus;

    @Column(nullable = false)
    double readingProgress;

    LocalDate addedDate;

    LocalDate startedDate;

    LocalDate finishedDate;
}
