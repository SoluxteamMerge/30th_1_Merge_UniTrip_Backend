package com.Solux.UniTrip.entity;

import com.Solux.UniTrip.dto.request.BoardRequest;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Column(nullable = false)
    private String scheduleDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int scraps;

    @Column(nullable = false)
    private int commentCount;



    //Image 엔티티 매핑
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Image> images = new ArrayList<>();

    // Place 엔티티 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    // Rating 엔티티 매핑
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    public Board(BoardRequest request, User user, PostCategory category, List<Image> images, Place place) {
        this.boardType = BoardType.valueOf(request.getBoardType());
        this.category = category;
        this.title = request.getTitle();
        this.content = request.getContent();
        this.user = user;
        this.createdAt = LocalDateTime.now();  // 생성 시점에 createdAt 설정
        this.scheduleDate = request.getScheduleDate();
        this.views = 0;
        this.likes = 0;
        this.scraps = 0;
        this.commentCount = 0;
        this.place = place;
        this.images = new ArrayList<>();

        for (Image image : images) {
            image.setBoard(this);
            this.images.add(image);
        }
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
    public void updateCommonFields(String title, String content, PostCategory category) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
        if (category != null) {
            this.category = category;
        }
        this.updatedAt = LocalDateTime.now();
    }


}