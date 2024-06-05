package com.example.goodreads_finalproject.model.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class  BookSearchRequest extends BaseSearchRequest {
    @Size(max = 100, message = "search cannot over 100 characters")
    String all;

    @Size(max = 100, message = "Title cannot over 100 characters")
    String title;

    @Size(max = 100, message = "Author cannot over 100 characters")
    String author;

    @Size(max = 100, message = "Category cannot over 100 characters")
    String category;

    @Size(max = 100, message = "Search cannot over 100 characters")
    String search;
}
