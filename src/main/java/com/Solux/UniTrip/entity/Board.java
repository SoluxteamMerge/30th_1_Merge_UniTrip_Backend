package com.Solux.UniTrip.entity;

import com.Solux.UniTrip.dto.request.BoardRequest;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int likes;

    // 여행 모집용 필드
    private String placeName;
    private String roadAddress;
    private String kakaoPlaceId;
    private Double latitude;
    private Double longitude;

    public Board(BoardRequest request, User user, PostCategory category) {
        this.boardType = BoardType.valueOf(request.getBoardType());
        this.category = category;
        this.title = request.getTitle();
        this.content = request.getContent();
        this.placeName = request.getPlaceName();
        this.roadAddress = request.getRoadAddress();
        this.kakaoPlaceId = request.getKakaoPlaceId();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.user = user;
        this.createdAt = LocalDateTime.now();  // 생성 시점에 createdAt 설정
        this.views = 0;
        this.likes = 0;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}