package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {

    String avatar;

    @NotBlank
    @Email(message = "Yêu cầu nhập đúng định dạng email")
    String email;

    @NotBlank
    String password;
}
