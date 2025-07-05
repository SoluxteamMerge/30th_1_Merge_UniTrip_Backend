package com.Solux.UniTrip.dto.request;

import com.Solux.UniTrip.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String name;
    private final String email;
    private String nickname;
    private User.UserType userType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String name, String email, String nickname, User.UserType userType) {
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.userType = userType;
    }

    public static OAuthAttributes of(Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String nickname = (String) attributes.getOrDefault("nickname", "defaultNickname");

        // userType은 attributes에 없으면 PERSONAL로 기본 지정
        String userTypeStr = (String) attributes.getOrDefault("userType", "PERSONAL");
        User.UserType userType;
        try {
            userType = User.UserType.valueOf(userTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            userType = User.UserType.PERSONAL;
        }

        return OAuthAttributes.builder()
                .attributes(attributes)
                .name(name)
                .email(email)
                .nickname(nickname)
                .userType(userType)
                .build();
    }
}