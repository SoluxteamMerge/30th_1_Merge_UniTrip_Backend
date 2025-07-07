package com.Solux.UniTrip.service;

import com.Solux.UniTrip.domain.Review;
import com.Solux.UniTrip.domain.UserTemp;
import com.Solux.UniTrip.dto.ReviewRequest;
import com.Solux.UniTrip.dto.ReviewResponse;
import com.Solux.UniTrip.repository.ReviewRepository;
import com.Solux.UniTrip.repository.UserTempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserTempRepository userRepository;

    public ReviewResponse createReview(ReviewRequest request) {
        // User 가져오기 (임시) → 나중엔 SecurityContext에서 로그인 사용자 가져옴
        UserTemp user = userRepository.findById(10L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review(request, user);
        Review saved = reviewRepository.save(review);

        return new ReviewResponse(200, saved.getId(), "리뷰가 성공적으로 작성되었습니다.");
    }
}
