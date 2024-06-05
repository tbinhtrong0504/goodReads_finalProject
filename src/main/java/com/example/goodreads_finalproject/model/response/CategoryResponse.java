package com.example.goodreads_finalproject.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {

    Long id;

    String name;
}
