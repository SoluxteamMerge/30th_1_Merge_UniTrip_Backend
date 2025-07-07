package com.Solux.UniTrip.common.apiPayload.exception;

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import lombok.Getter;

@Getter
public class ScheduleConflictException extends RuntimeException {
  private final FailureStatus failureStatus;

  public ScheduleConflictException() {
    super(FailureStatus._SCHEDULE_CONFLICT.getMessage());
    this.failureStatus = FailureStatus._SCHEDULE_CONFLICT;
  }
}
