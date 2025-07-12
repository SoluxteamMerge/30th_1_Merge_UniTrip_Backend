package com.Solux.UniTrip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileRequest {

    @NotBlank(message = "닉네임 입력은 필수입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "닉네임은 공백 없이 2~20자의 한글, 영문, 숫자만 사용 가능합니다.")
    private String nickname;

    @Pattern(regexp = "^010\\d{7,8}$", message = "유효한 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @NotBlank(message = "유저 타입 입력은 필수입니다.")
    private String userType;

    //boolean 타입에는 not null
    @NotNull(message = "학교 이메일 인증은 필수입니다.")
    private boolean emailVerified;

}
