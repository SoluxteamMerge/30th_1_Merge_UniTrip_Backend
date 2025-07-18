package com.Solux.UniTrip.common.apiPayload.exception;

import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;

public class UploadFailureExpection extends RuntimeException{
    private final FailureStatus failureStatus;

    public UploadFailureExpection() {
        super(FailureStatus._PROFILEIMAGE_UPLOAD_FAILURE.getMessage());
        this.failureStatus = FailureStatus._PROFILEIMAGE_UPLOAD_FAILURE;
    }

    public UploadFailureExpection(FailureStatus failureStatus) {
        super(failureStatus.getMessage());
        this.failureStatus = failureStatus;
    }

    public FailureStatus getFailureStatus() {
        return failureStatus;
    }
}




