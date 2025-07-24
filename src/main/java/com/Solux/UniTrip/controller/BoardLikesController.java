package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.service.BoardLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BoardLikesController {
    private final BoardLikesService boardLikesService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<BoardResponse> toggleLike(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        BoardResponse response = boardLikesService.toggleLike(postId, user);
        return ResponseEntity.ok(response);
    }

}