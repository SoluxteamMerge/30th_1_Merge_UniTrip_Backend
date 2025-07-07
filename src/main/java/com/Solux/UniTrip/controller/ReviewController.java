package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.dto.ReviewRequest;
import com.Solux.UniTrip.dto.ReviewResponse;
import com.Solux.UniTrip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.ok(response);
    }
}