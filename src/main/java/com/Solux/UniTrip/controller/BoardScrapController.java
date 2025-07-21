package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.service.BoardScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BoardScrapController {

    private final BoardScrapService boardScrapService;

    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<BoardResponse> toggleScrap(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        BoardResponse response = boardScrapService.toggleScrap(postId, user);
        return ResponseEntity.ok(response);
    }

}