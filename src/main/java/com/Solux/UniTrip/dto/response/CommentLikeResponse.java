package com.Solux.UniTrip.dto.response;
//댓글 좋아요 response DTO

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeResponse {
    private Long commentId;
    private int likeCount;

    @JsonIgnore
    private boolean liked;

    @JsonProperty("isLiked")
    public boolean isLiked() {
        return liked;
    }
}
