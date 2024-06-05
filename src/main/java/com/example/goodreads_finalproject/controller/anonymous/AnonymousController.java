package com.example.goodreads_finalproject.controller.anonymous;

import com.example.goodreads_finalproject.exception.OtpExpiredException;
import com.example.goodreads_finalproject.model.request.EmailRequest;
import com.example.goodreads_finalproject.model.request.ResetPasswordRequest;
import com.example.goodreads_finalproject.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/anonymous")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnonymousController {

    UserService userService;

    @PostMapping("/otp-sending")
    public ResponseEntity<?> sendOtp(@RequestBody @Valid EmailRequest emailRequest) {
        return userService.findByEmailAndActivated(emailRequest.getEmail())
                .map(user -> {
                    userService.sendOtp(emailRequest.getEmail());
                    return new ResponseEntity<>(null, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>("Email not exist or not activated", HttpStatus.NOT_FOUND));
    }

    @PutMapping("/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws OtpExpiredException {
        userService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>("Change password successful", HttpStatus.OK);
    }

}
