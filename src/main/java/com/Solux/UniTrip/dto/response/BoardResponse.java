package com.Solux.UniTrip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardResponse {
    private int status;
    private Long postId;
    private String message;
}