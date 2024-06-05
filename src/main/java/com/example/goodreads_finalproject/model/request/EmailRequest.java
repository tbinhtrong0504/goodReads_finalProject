package com.example.goodreads_finalproject.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    @NotBlank
    @Email
    String email;

}
