package com.Solux.UniTrip.common.apiPayload.base;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDTO {
    private final int code;
    private final String message;
    private final Object data;
}