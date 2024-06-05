package com.example.goodreads_finalproject.model.request;

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
public class ReadingBookRequest extends BaseSearchRequest {

    Long userId;

    Long bookId;

    String readingStatus;

    double readingProgress;

    LocalDate startedDate;

    LocalDate finishedDate;

}
