package com.Solux.UniTrip.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class BoardRequest {
    private String boardType;
    private String categoryName;
    private String title;
    private String content;
    private String thumbnailUrl;

    private String placeName;
    private String roadAddress;
    private String kakaoPlaceId;
    private Double latitude;
    private Double longitude;

    private Boolean overnightFlag;
    private Integer recruitmentCnt;

}