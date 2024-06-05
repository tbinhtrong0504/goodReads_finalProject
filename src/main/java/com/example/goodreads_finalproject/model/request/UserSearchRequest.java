package com.example.goodreads_finalproject.model.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserSearchRequest extends BaseSearchRequest {
    @Size(max = 100, message = "Email cannot over 100 characters")
    String email;

    @Size(max = 100, message = "Name cannot over 100 characters")
    String fullName;
}
