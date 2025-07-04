package com.Solux.UniTrip.comment.dto;
//댓글 response DTO
//백다현

import com.Solux.UniTrip.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int likeCount;

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId() != null ? comment.getPostId() : 0L)
                .content(comment.getContent() != null ? comment.getContent() : "")
                // TODO: 로그인 구현되면 userId로 User 테이블 조회해서 이름으로 변환
                .author(comment.getUserId() != null ? String.valueOf(comment.getUserId()) : "알 수 없음")
                .createdAt(comment.getCreatedAt() != null ? comment.getCreatedAt() : LocalDateTime.now())
                .likeCount(comment.getLikeCount())
                .build();
    }

}
