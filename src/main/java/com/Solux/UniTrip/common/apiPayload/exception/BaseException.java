package com.Solux.UniTrip.common.apiPayload.exception;

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final FailureStatus failureStatus;

    public BaseException(FailureStatus failureStatus) {
        super(failureStatus.getMessage());
        this.failureStatus = failureStatus;
    }
}
