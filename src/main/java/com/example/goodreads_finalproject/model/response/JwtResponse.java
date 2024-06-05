package com.example.goodreads_finalproject.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtResponse {

    String jwt;

    String refreshToken;

    Long id;

    String email;

    Set<String> roles;

    String avatar;

    String fullName;

    boolean locked;

}
