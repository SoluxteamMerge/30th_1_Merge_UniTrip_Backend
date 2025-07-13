// 개별 리뷰 항목 DTO
package com.Solux.UniTrip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardItemResponse {
    private Long postId;
    private String boardType;
    private String categoryName;
    private String title;
    private String content;
    private Long userId;
    private String nickname;
    private String createdAt;
    private int commentCount;
    private int likes;
    private int scrapCount;
    private boolean isLiked;
    private boolean isScraped;
    private String thumbnailUrl;
}