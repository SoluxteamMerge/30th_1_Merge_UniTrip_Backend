package com.Solux.UniTrip.common.dto;
//API 에러시 response

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private int code;
    private String message;
}
