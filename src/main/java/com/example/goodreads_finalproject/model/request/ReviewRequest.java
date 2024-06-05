package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {

    @NotNull
    Long bookId;

    @Max(value = 5)
    double rating;

    @Size(max = 65535, message = "content cannot over 65.535 characters")
    String content;

    String readingStatus;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    LocalDate startedDate;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    LocalDate finishedDate;
}
