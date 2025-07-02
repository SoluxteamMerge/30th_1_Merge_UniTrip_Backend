package com.Solux.UniTrip.comment.entity;
//Comment 엔티티
//백다현

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;  // 로그인 개발되면 이 부분 연동

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment(Long postId, Long userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.likeCount = 0;
    }

    //댓글 수정 메소드
    public void updateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("수정할 내용이 비어있습니다.");
        }
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

}