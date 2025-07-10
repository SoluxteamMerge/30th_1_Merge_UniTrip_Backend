package com.Solux.UniTrip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google")
public class AuthController {

    @GetMapping("/login")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return Map.of("message", "User not authenticated");
        }

        return Map.of(
                "name", oAuth2User.getAttribute("name"),
                "email", oAuth2User.getAttribute("email"),
                "token", oAuth2User.getAttribute("jwtToken")
        );
    }
}

