package com.example.goodreads_finalproject.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonResponse<T> {
    Integer totalResult;
    Integer pageNumber;
    T data;
}