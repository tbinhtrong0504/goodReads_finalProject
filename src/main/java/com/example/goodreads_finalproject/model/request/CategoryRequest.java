package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest extends BaseSearchRequest{

    @NotBlank
    @Size(max = 100, message = "Name cannot over 100 characters")
    String name;
}
