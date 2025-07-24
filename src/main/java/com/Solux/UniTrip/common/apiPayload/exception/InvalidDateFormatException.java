package com.Solux.UniTrip.common.apiPayload.exception;
//유효하지 않은 날짜 형식

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import lombok.Getter;

@Getter
public class InvalidDateFormatException extends RuntimeException {
    private final FailureStatus failureStatus;

    public InvalidDateFormatException() {
        super(FailureStatus._INVALID_DATE_FORMAT.getMessage());
        this.failureStatus = FailureStatus._INVALID_DATE_FORMAT;
    }
}
