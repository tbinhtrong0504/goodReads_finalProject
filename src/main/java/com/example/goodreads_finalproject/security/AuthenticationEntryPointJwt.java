package com.example.goodreads_finalproject.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationEntryPointJwt implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        log.info("ádfasfasf");
        response.sendRedirect("http://localhost:8080/login");
    }
}