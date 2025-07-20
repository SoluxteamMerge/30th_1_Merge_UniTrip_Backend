package com.Solux.UniTrip.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;;

@Getter
@NoArgsConstructor
public class UserInfoResponse {
    private String nickname;
    private String profileImageUrl;

    public UserInfoResponse(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
