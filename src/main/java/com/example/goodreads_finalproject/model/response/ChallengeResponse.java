package com.example.goodreads_finalproject.model.response;

import com.example.goodreads_finalproject.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChallengeResponse {

    Long challengeId;

    Integer numberOfBook;

    Integer booksRead;

    LocalDate startedDate;

    LocalDate endDate;

}
