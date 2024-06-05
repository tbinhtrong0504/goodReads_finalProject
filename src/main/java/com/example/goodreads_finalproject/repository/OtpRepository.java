package com.example.goodreads_finalproject.repository;

import com.example.goodreads_finalproject.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByOtpCode(String otpCode);

}
