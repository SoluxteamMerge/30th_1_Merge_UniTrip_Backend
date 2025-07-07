package com.Solux.UniTrip.domain;

import com.Solux.UniTrip.dto.ReviewRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardType;
    private String categoryName;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String placeName;
    private String roadAddress;
    private String kakaoPlaceId;
    private Double latitude;
    private Double longitude;

    private Boolean overnightFlag;
    private Integer recruitmentCnt;

    // 작성자 (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserTemp user;

    // 생성자
    public Review(ReviewRequest request, UserTemp user) {
        this.boardType = request.getBoardType();
        this.categoryName = request.getCategoryName();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.placeName = request.getPlaceName();
        this.roadAddress = request.getRoadAddress();
        this.kakaoPlaceId = request.getKakaoPlaceId();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.overnightFlag = request.getOvernightFlag();
        this.recruitmentCnt = request.getRecruitmentCnt();
        this.user = user;
    }
}