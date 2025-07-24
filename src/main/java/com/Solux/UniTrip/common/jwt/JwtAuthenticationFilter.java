package com.Solux.UniTrip.common.jwt;

import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        // 인증 없이 허용되는 경로들
        boolean isPublicPath =
                path.startsWith("/api/google/login") ||
                        path.startsWith("/oauth2") ||
                        path.startsWith("/public") ||
                        path.startsWith("/swagger") ||
                        path.startsWith("/v3/api-docs") ||
                        path.startsWith("/swagger-ui") ||
                        path.startsWith("/webjars") ||

                        // GET만 허용되는 경로
                        ("GET".equals(method) && (
                                path.startsWith("/api/comments") ||
                                        path.equals("/api/schedules") ||
                                        path.matches("^/api/schedules/\\d+$") ||
                                        path.equals("/api/keywords/popular") ||

                                        // 리뷰 GET만 예외
                                        path.equals("/api/reviews") ||
                                        path.matches("^/api/reviews\\?boardType=.*$") ||
                                        path.matches("^/api/reviews/\\d+$") ||
                                        path.startsWith("/api/reviews/search") ||
                                        path.startsWith("/api/reviews/popular") ||
                                        path.startsWith("/api/reviews/filter")
                        )) ||

                        // 이메일 인증, 키워드 랭킹 POST
                        ("POST".equals(method) && path.equals("/api/keywords/rank")) ||
                        path.equals("/api/user/email") ||
                        path.equals("/api/user/email/verify");

        if (isPublicPath) {
            // ✅ 토큰이 있으면 무조건 인증 처리
            tryAuthenticateIfTokenPresent(request);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 추출 및 검증
        String token = resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            sendUnauthorizedResponse(response);
            return;
        }

        // 사용자 인증 처리
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            sendUnauthorizedResponse(response);
            return;
        }

        request.setAttribute("user", user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            if (header.startsWith("Bearer ")) {
                return header.substring(7).trim();
            } else {
                return header.trim(); // Bearer 없이도 허용
            }
        }
        return null;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", "인증되지 않은 사용자입니다.");
        result.put("data", null);

        new ObjectMapper().writeValue(response.getOutputStream(), result);
    }

    private void tryAuthenticateIfTokenPresent(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }
}

