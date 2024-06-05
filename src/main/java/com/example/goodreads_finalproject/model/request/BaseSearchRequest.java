package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseSearchRequest {
    Integer pageIndex = 1;
    Integer pageSize = 5;
}
