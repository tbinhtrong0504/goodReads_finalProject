package com.example.goodreads_finalproject.controller;

import com.example.goodreads_finalproject.entity.RefreshToken;
import com.example.goodreads_finalproject.entity.User;
import com.example.goodreads_finalproject.exception.AccountNotActiveException;
import com.example.goodreads_finalproject.exception.BadRequestException;
import com.example.goodreads_finalproject.exception.RefreshTokenNotFoundException;
import com.example.goodreads_finalproject.model.request.*;
import com.example.goodreads_finalproject.model.response.JwtResponse;
import com.example.goodreads_finalproject.repository.RefreshTokenRepository;
import com.example.goodreads_finalproject.repository.UserRepository;
import com.example.goodreads_finalproject.security.CustomUserDetails;
import com.example.goodreads_finalproject.security.JwtUtils;
import com.example.goodreads_finalproject.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authentication")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    JwtUtils jwtUtils;

    UserService userService;

    UserRepository userRepository;

    RefreshTokenRepository refreshTokenRepository;

    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        User user = userRepository.findById(userDetails.getId()).get();

        if (!user.isActivated()) {
            throw new AccountNotActiveException("Account not activated");
        }

        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        if (user.isLocked()) {
            return JwtResponse.builder()
                    .locked(user.isLocked())
                    .build();
        }

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwt(jwt)
                .refreshToken(refreshToken)
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .roles(roles)
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .locked(user.isLocked())
                .build();
        Cookie jwtCookie = new Cookie("jwtToken", jwtResponse.getJwt());
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        return jwtResponse;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest request) {

        return userRepository.findByEmail(request.getEmail())
                .map(user -> new ResponseEntity<>("Email is existed", HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    userService.registerUser(request);
                    return new ResponseEntity<>(null, HttpStatus.CREATED);
                });
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        userService.logout();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest request, HttpServletResponse response) {
        try {
            return ResponseEntity.ok(userService.refreshToken(request, response));
        } catch (RefreshTokenNotFoundException | UsernameNotFoundException ex) {
            return new ResponseEntity<>("Thông tin refreshToken không chính xác", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/password-change")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        try {
            userService.changePassword(request);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("Thay đổi mật khẩu không thành công!", HttpStatus.BAD_REQUEST);
        }
    }
}
