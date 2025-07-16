package com.Solux.UniTrip.dto.request;
//이메일 인증 요청

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequest {

    @NotBlank
    @Email
    private String email;
}