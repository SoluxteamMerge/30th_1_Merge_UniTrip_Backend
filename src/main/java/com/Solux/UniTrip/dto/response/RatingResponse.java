package com.Solux.UniTrip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RatingResponse {
    private Long postId;
    private Double rating;      // 삭제된 경우 null
    private boolean isRated;    // 등록/수정: true, 삭제: false
}
