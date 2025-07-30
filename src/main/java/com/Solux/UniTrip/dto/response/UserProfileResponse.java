package com.Solux.UniTrip.dto.response;

import com.Solux.UniTrip.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileResponse {
    private boolean isProfileRegistered;
    private String name;
    private String nickname;
    private String phoneNumber;
    private User.UserType userType;
    private boolean emailVerified;

    public UserProfileResponse(String name, String nickname, String phoneNumber, User.UserType userType, boolean emailVerified) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.emailVerified = emailVerified;
        this.isProfileRegistered = !nickname.equals("defaultNickname");
    }
}
