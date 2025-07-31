// 개별 리뷰 항목 DTO
package com.Solux.UniTrip.dto.response;

import jakarta.persistence.Column;
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
    private String profileImageUrl;
    private String createdAt;
    private String scheduleDate;
    private int commentCount;
    private int views;
    private double rating;
    private int likes;
    private int scraps;
    private boolean isLiked;
    private boolean isScraped;
    private String imageUrl;
    private String placeName;
    private String address;
    private String kakaoId;
    private String categoryGroupName;
    private String region;
    private int scrapCount;
}