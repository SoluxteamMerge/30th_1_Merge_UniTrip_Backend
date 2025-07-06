package com.Solux.UniTrip.common.apiPayload.base;

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "data"})
public class ApiResponse<T> {
    private final Integer code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> onSuccess(T data, SuccessStatus successStatus) {
        return new ApiResponse<>(successStatus.getCode(), successStatus.getMessage(), data);
    }

    public static <T> ApiResponse<T> onFailure(T data, FailureStatus failureStatus) {
        return new ApiResponse<>(failureStatus.getCode(), failureStatus.getMessage(), data);
    }

    public static <T> ApiResponse<T> of(T data, String message, Integer code) {
        return new ApiResponse<>(code, message, data);
    }
}
