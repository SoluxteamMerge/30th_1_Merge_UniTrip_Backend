package com.Solux.UniTrip.service;

import com.Solux.UniTrip.dto.request.OAuthAttributes;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        String userInfoEndpointUri = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();

        if (!StringUtils.hasText(userInfoEndpointUri)) {
            throw new OAuth2AuthenticationException("User Info Endpoint URI is missing");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getTokenValue());

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = new RestTemplate().exchange(
                userInfoEndpointUri,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> userAttributes = response.getBody();
        if (userAttributes == null) {
            throw new OAuth2AuthenticationException("Failed to retrieve user attributes");
        }

        OAuthAttributes attributes = OAuthAttributes.of(userAttributes);
        User user = saveOrUpdate(attributes);

        String jwtToken = generateJwtToken(user);

        // JWT 토큰과 사용자 정보를 attributes에 추가 (필요 시)
        userAttributes.put("jwtToken", jwtToken);
        userAttributes.put("user", Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "userType", user.getUserType().name()
        ));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                "name"
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        return userRepository.findByEmail(attributes.getEmail())
                .map(entity -> {
                    // 업데이트 시에도 nickname, userType을 null이 아닐 값으로 넣어줘야 함
                    String nickname = attributes.getNickname() != null ? attributes.getNickname() : entity.getNickname();
                    User.UserType userType = attributes.getUserType() != null ? attributes.getUserType() : entity.getUserType();

                    entity.update(attributes.getName(), attributes.getEmail(), nickname, userType);
                    return userRepository.save(entity);
                })
                .orElseGet(() -> userRepository.save(createNewUser(attributes)));
    }

    private User createNewUser(OAuthAttributes attributes) {
        return User.builder()
                .name(attributes.getName())
                .email(attributes.getEmail())
                .nickname(attributes.getNickname() != null ? attributes.getNickname() : "defaultNickname")
                .userType(attributes.getUserType() != null ? attributes.getUserType() : User.UserType.PERSONAL)
                .emailVerified(false)
                .createdAt(java.time.LocalDateTime.now())
                .build();
    }

    private String generateJwtToken(User user) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("name", user.getName())
                .claim("role", "ROLE_USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

