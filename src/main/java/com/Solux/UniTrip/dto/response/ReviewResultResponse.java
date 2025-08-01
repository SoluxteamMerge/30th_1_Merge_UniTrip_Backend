package com.Solux.UniTrip.dto.response;

import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Image;
import com.Solux.UniTrip.entity.Rating;
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
    private Double rating;
    private int likeCount;
    private int scrapCount;
    private String categoryName;
    private String boardType;

    public static ReviewResultResponse from(Board board) {
        List<Image> images = board.getImages();
        String imageUrl = (images != null && !images.isEmpty()) ? images.get(0).getImageUrl() : null;

        // 평균 평점 계산
        List<Rating> ratings = board.getRatings();
        double averageRating = 0.0;
        if (ratings != null && !ratings.isEmpty()) {
            averageRating = ratings.stream()
                    .mapToDouble(Rating::getRating)
                    .average()
                    .orElse(0.0);

            // 소수점 첫째 자리로 반올림
            averageRating = Math.round(averageRating * 10) / 10.0;
        }

        return ReviewResultResponse.builder()
                .imageUrl(imageUrl)
                .nickname(board.getUser().getNickname())
                .postId(board.getPostId())
                .postTitle(board.getTitle())
                .rating(averageRating)
                .likeCount(board.getLikes())
                .scrapCount(board.getScraps())
                .categoryName(board.getCategory().getCategoryName())
                .boardType(board.getBoardType().toString())
                .build();
    }
}
