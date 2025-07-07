package com.Solux.UniTrip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private int status;
    private Long postId;
    private String message;
}