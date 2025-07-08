package com.Solux.UniTrip.common.apiPayload.exception;

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
  private final FailureStatus failureStatus;

  public ForbiddenException() {
    super(FailureStatus.FORBIDDEN.getMessage());
    this.failureStatus = FailureStatus.FORBIDDEN;
  }
}
