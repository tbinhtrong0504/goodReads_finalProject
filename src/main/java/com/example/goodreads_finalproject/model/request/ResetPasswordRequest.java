package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    @NotBlank
    String otpCode;

    @NotBlank
    @Size(max = 100, message = "Password cannot over 100 characters")
    String newPassword;
}
