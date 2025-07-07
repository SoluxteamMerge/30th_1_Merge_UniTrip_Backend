package com.Solux.UniTrip.dto.response;
//댓글 좋아요 response DTO
//백다현

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeResponse {
    private Long commentId;
    private boolean isLiked;
    private int likeCount;
}
