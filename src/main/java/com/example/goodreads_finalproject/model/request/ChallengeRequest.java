package com.example.goodreads_finalproject.model.request;

import com.example.goodreads_finalproject.statics.ChallengeStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChallengeRequest extends BaseSearchRequest {

    @NotNull
    @Min(value = 1)
    Integer numberOfBook;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startedDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate;
}
