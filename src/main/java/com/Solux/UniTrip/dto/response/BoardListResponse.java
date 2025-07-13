// 개별 리뷰 항목 DTO
package com.Solux.UniTrip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class BoardListResponse {
    private int total;
    private List<BoardItemResponse> reviews;
}