package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/google")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public ApiResponse<LoginResponse> getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ApiResponse.onFailure(null, FailureStatus._UNAUTHORIZED);
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String token = jwtTokenProvider.createToken(email);

        LoginResponse loginResponse = new LoginResponse(token, name, email);
        return ApiResponse.onSuccess(loginResponse, SuccessStatus._LOGIN_SUCCESS);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        //토큰이 삭제되면 로그아웃 처리
        return ApiResponse.onSuccess(null, SuccessStatus._LOGOUT_SUCCESS);
    }

}

