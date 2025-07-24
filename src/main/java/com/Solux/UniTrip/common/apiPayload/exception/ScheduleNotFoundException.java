package com.Solux.UniTrip.common.apiPayload.exception;

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import lombok.Getter;

@Getter
public class ScheduleNotFoundException extends RuntimeException {
    private final FailureStatus failureStatus;

    public ScheduleNotFoundException() {
        super(FailureStatus.SCHEDULE_NOT_FOUND.getMessage());
        this.failureStatus = FailureStatus.SCHEDULE_NOT_FOUND;
    }
}

