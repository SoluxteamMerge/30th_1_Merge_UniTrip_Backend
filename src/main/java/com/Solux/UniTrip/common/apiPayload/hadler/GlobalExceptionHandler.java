package com.Solux.UniTrip.common.apiPayload.hadler;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getFailureStatus().getCode())
                .body(ApiResponse.onFailure(null, e.getFailureStatus()));
    }
}
