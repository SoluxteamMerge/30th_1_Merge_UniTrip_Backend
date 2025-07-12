package com.Solux.UniTrip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileModifyRequest {

    private String nickname;

    @Pattern(regexp = "^010\\d{7,8}$", message = "유효한 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    private String userType;

    private boolean emailVerified;

}
