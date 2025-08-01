package com.Solux.UniTrip.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequest {
    private String boardType;
    private String categoryName;
    private String title;
    private String content;
    private String scheduleDate;
    private String imageUrl;

    private String placeName;
    private String address;
    private String kakaoId;
    private String categoryGroupName;
    private String region;
    private Float lat;
    private Float lng;

}