package com.Solux.UniTrip.dto.response;
//댓글 response DTO
//백다현

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
}
