package ru.academits.phonebookhibernate.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CurrentUser {
    public Long getId() {
        return getUserDetails()
                .map(ApplicationUserDetails::getUserId)
                .orElseThrow(() -> new IllegalStateException("User is not authenticated"));
    }

    public boolean isAdmin() {
        return getUserDetails()
                .map(userDetails -> userDetails.hasRole(UserRole.ROLE_ADMIN))
                .orElse(false);
    }

    private Optional<ApplicationUserDetails> getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }

        if (auth.getPrincipal() instanceof ApplicationUserDetails userDetails) {
            return Optional.of(userDetails);
        }

        return Optional.empty();
    }
}