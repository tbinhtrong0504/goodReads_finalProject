package com.example.goodreads_finalproject.config;

import com.example.goodreads_finalproject.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaAuditingConfig implements AuditorAware<String> {

    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        } else {
            try {
//                Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                return Optional.of(userDetails.getUsername());
            } catch (Exception e) {
                return Optional.empty();
            }
        }


    }

}
