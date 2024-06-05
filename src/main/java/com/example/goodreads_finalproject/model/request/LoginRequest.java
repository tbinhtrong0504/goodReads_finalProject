package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank
    @Email (message = "Email format is incorrect")
    String email;

    @NotBlank
    String password;

}
