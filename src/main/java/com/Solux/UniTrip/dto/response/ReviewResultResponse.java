package com.Solux.UniTrip.dto.response;

import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewResultResponse {
    private String imageUrl;
    private String nickname;
    private Long postId;
    private String postTitle;
    private Integer commentCount;
    private Integer likeCount;
    private Integer scrapCount;
    private String categoryName;

    public static ReviewResultResponse from(Board board) {
        List<Image> images = board.getImages();
        String imageUrl = (images != null && !images.isEmpty()) ? images.get(0).getImageUrl() : null;
        return ReviewResultResponse.builder()
                .imageUrl(imageUrl)
                .nickname(board.getUser().getNickname())
                .postId(board.getPostId())
                .postTitle(board.getTitle())
                .likeCount(board.getLikes())
                .scrapCount(board.getScraps())
                .categoryName(board.getCategory().getCategoryName())
                .build();
    }
}
