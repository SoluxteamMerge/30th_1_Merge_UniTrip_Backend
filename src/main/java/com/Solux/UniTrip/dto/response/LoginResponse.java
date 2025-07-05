package com.Solux.UniTrip.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String name;
    private String email;
    private String token;
}
