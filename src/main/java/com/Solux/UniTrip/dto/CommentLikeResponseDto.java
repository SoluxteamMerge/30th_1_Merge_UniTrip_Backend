package com.Solux.UniTrip.dto;
//댓글 좋아요 response DTO
//백다현

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeResponseDto {
    private Long commentId;
    private boolean isLiked;
    private int likeCount;
}
