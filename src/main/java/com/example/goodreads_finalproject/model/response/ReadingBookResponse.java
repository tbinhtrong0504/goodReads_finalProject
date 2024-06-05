package com.example.goodreads_finalproject.model.response;

import com.example.goodreads_finalproject.entity.BaseEntity;
import com.example.goodreads_finalproject.entity.Book;
import com.example.goodreads_finalproject.entity.User;
import com.example.goodreads_finalproject.statics.ReadingStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadingBookResponse{

    Book book;

    String readingStatus;

    double readingProgress;

    LocalDate addedDateTime;

    LocalDate startedDateTime;

    LocalDate finishedDateTime;

}
