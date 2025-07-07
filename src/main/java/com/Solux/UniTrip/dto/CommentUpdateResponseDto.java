package com.Solux.UniTrip.dto;
//댓글 수정 response dto
//백다현

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentUpdateResponseDto {
    private Long commentId;
    private String content;
    private LocalDateTime updatedAt;
}
