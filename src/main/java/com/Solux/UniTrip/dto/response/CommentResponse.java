package com.Solux.UniTrip.dto.response;
//댓글 response DTO
//백다현

import com.Solux.UniTrip.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int likeCount;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPostId() != null ? comment.getPostId() : 0L)
                .content(comment.getContent() != null ? comment.getContent() : "")
                .author(comment.getUserId() != null ? String.valueOf(comment.getUserId()) : "알 수 없음")
                .createdAt(comment.getCreatedAt() != null ? comment.getCreatedAt() : LocalDateTime.now())
                .likeCount(comment.getLikeCount())
                .build();
    }

}
