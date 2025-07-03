package com.Solux.UniTrip.common.dto;
//API 성공시 response

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiSuccessResponse<T> {
    private int code;
    private String message;
    private T data;
}
