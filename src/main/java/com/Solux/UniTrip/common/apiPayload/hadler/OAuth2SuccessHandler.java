package com.Solux.UniTrip.common.apiPayload.hadler;

import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider; // JWT 생성 서비스 (generateJwtToken 메서드 분리 권장)

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        // 토큰 생성 (UserService 또는 별도 Provider에서 구현 권장)
        String token = jwtTokenProvider.createToken(email);

        // 토큰을 쿠키, 헤더, 혹은 리다이렉션 URL 파라미터로 전달
        response.sendRedirect("http://localhost:8080/oauth2/success?token=" + token);
    }
}
