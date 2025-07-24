package com.Solux.UniTrip.dto.request;
//이메일 인증 코드 검증

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailVerifyRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String code;
}
