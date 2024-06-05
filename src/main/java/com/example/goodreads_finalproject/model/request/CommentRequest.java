package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {

    @NotNull
    Long reviewId;

    @Size(max = 65535, message = "Content cannot over 65.535 characters")
    String content;

}
