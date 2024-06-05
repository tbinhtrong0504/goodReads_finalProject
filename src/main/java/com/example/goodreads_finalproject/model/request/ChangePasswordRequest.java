package com.example.goodreads_finalproject.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank
    @Size(max = 100, message = "Current password cannot over 100 characters")
    String currentPassword;

    @NotBlank
    @Size(max = 100, message = "New password cannot over 100 characters")
    @Size(min = 6, message = "New password must have at least 6 characters")
    String newPassword;

    @NotBlank
    @Size(max = 100, message = "Re-Password cannot over 100 characters")
    String rePassword;

    @AssertTrue(message = "Passwords do not match")
    private boolean isPasswordMatch() {
        return newPassword != null && newPassword.equals(rePassword);
    }
}
