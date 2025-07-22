package com.Solux.UniTrip.common.apiPayload.hadler;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.*;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getFailureStatus().getCode())
                .body(ApiResponse.onFailure(null, e.getFailureStatus()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException e) {
        return ResponseEntity
                .status(e.getFailureStatus().getCode())
                .body(ApiResponse.onFailure(null, e.getFailureStatus()));
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ScheduleNotFoundException e) {
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
                .body(ApiResponse.onFailure(null, FailureStatus._BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.onFailure(null, FailureStatus._BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled Exception occurred: ", e); // 로그 추가
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.onFailure(null, FailureStatus._INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
