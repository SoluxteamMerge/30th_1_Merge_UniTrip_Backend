package com.Solux.UniTrip.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
<<<<<<< Updated upstream
                        .requestMatchers("/api/comments/**").permitAll()
=======
                        .requestMatchers( "/", "/css/**", "/js/**", "/images/**",
                                "/api/place/search").permitAll()

                        .requestMatchers("/api/comments/**").authenticated()

>>>>>>> Stashed changes
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}

