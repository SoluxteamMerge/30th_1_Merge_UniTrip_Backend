package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
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

    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        //토큰은 서버에 상태를 저장하지 않기 때문에 기간이 만료되면 토큰이 삭제됨
        //토큰 삭제 되면 자동 로그아웃 처리
        return ApiResponse.onSuccess(null, SuccessStatus._LOGOUT_SUCCESS);
    }
}
