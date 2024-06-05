package com.example.goodreads_finalproject.model.request;

import com.example.goodreads_finalproject.statics.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    boolean locked;

    String avatar;

    @Size(max = 100, message = "Name cannot over 100 characters")
    String fullName;

    String gender;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @Past(message = "Date of birth must be in the past")
    LocalDate dob;

    @Pattern(regexp = "\\d{10}|^$", message = "Phone must be 10 number characters")
    String phone;

    String address;

    @Size(max = 65535, message = "About cannot over 65.535 characters")
    String about;

    String Role;
}
