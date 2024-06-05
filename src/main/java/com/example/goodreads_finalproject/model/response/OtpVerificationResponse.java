package com.example.goodreads_finalproject.model.response;

import com.example.goodreads_finalproject.entity.Otp;
import com.example.goodreads_finalproject.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpVerificationResponse {
    Otp otp;

    boolean success;

    Integer timesVerification;

}
