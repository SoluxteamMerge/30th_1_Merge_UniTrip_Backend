package com.Solux.UniTrip.common.apiPayload.hadler;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.*;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
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

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDateFormat(InvalidDateFormatException e) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.onFailure(null, e.getFailureStatus()));
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDateRange(InvalidDateRangeException e) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.onFailure(null, e.getFailureStatus()));
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException e) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.onFailure(null, FailureStatus._BAD_REQUSET));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.onFailure(null, FailureStatus._BAD_REQUSET));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.onFailure(null, FailureStatus._INTERNAL_SERVER_ERROR));
    }
}
