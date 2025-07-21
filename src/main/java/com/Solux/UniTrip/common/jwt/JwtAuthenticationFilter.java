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

        // 인증 필요 없는 경로 예외 처리
        if (path.startsWith("/api/google/login")
                || path.startsWith("/oauth2")
                || path.startsWith("/public")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/webjars")
                || ("GET".equals(method) && (path.startsWith("/api/comments")
                || path.equals("/api/schedules")         //일정 목록 조회
                || path.matches("^/api/schedules/\\d+$") // 일정 상세 조회
                || path.equals("/api/keywords/popular")))
                || path.startsWith("/api/reviews")
                || ("POST".equals(method) && path.equals("/api/keywords/rank"))
                || path.equals("/api/user/email")
                || path.equals("/api/user/email/verify")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        // 토큰 없거나 유효하지 않은 경우
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            // /api/reviews/** 경로는 인증 안 되어도 401 반환하지 않고 통과시킴
            if (path.startsWith("/api/reviews")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 나머지 경로는 401 반환
            sendUnauthorizedResponse(response);
            return;
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // /api/reviews/** 경로는 인증 안 되어도 401 반환하지 않고 통과시킴
            if (path.startsWith("/api/reviews")) {
                filterChain.doFilter(request, response);
                return;
            }
            sendUnauthorizedResponse(response);
            return;
        }

        // SecurityContext에 인증 정보 저장
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
                // Bearer 없이도 허용
                return header.trim();
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
}