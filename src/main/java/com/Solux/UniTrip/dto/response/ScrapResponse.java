package com.Solux.UniTrip.dto.response;

import com.Solux.UniTrip.entity.Board;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ScrapResponse {

    private String thumbnailUrl;
    private String nickname;
    private Long postId;
    private String postTitle;
    private Integer commentCount;
    private Integer likeCount;
    private Integer scrapCount;
    private String categoryName;

    public static ScrapResponse from(Board board) {
        return ScrapResponse.builder()
                //.thumbnailUrl(board.getThumbnailUrl())
                .nickname(board.getUser().getNickname())
                .postId(board.getPostId())
                .postTitle(board.getTitle())
                //.commentCount(board.getCommentList().size())
                //.likeCount(board.getLikeCount())
                //.scrapCount(board.getScrapCount())
                .categoryName(board.getCategory().getCategoryName())
                .build();
    }
}
