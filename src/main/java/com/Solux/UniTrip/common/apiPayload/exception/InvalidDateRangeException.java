package com.Solux.UniTrip.common.apiPayload.exception;
//시작일이 종료일보다 이후일 때

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import lombok.Getter;

@Getter
public class InvalidDateRangeException extends RuntimeException {
    private final FailureStatus failureStatus;

    public InvalidDateRangeException() {
        super(FailureStatus._INVALID_DATE_RANGE.getMessage());
        this.failureStatus = FailureStatus._INVALID_DATE_RANGE;
    }
}
