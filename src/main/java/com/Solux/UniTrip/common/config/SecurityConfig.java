package com.Solux.UniTrip.common.config;

import com.Solux.UniTrip.common.apiPayload.hadler.OAuth2SuccessHandler;
import com.Solux.UniTrip.common.jwt.JwtAuthenticationFilter;
import com.Solux.UniTrip.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정 추가
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOrigins(List.of(
                            //개발용
                            "http://localhost:5173",
                            "http://52.79.206.60:5173",

                            //배포 도메인
                            "https://unitrip.duckdns.org"
                    ));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/keywords/popular").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments", "/api/reviews/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**", "/oauth2/success", "/google/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/keywords/rank","/api/user/email/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/reviews",
                                "/api/reviews/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/reviews/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/reviews/**"
                        ).authenticated()

                        .requestMatchers(HttpMethod.GET,
                                "/api/schedules",
                                "/api/schedules/*").authenticated()

                        .requestMatchers(
                                "/api/comments/**",
                                "/api/place/search",
                                "/api/user/**").authenticated()

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            exception.printStackTrace(); // 콘솔에 예외 출력
                            response.sendRedirect("https://unitrip.duckdns.org/oauth2/success?error=true");
                        })
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}