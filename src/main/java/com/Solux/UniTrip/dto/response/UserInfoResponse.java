package com.Solux.UniTrip.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserInfoResponse {
    private String name;
    private String nickname;
    private String profileImageUrl;

    public UserInfoResponse(String name, String nickname, String profileImageUrl) {
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
