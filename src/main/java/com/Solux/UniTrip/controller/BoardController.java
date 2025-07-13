package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.response.BoardListResponse;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.Solux.UniTrip.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal User user) {
        BoardResponse response = boardService.createBoard(request, user);
        return ResponseEntity.ok(response);
    }

    // 리뷰 조회(청춘카드)
    @GetMapping
    public ResponseEntity<BoardListResponse> getAllReviews() {
        BoardListResponse response = boardService.getAllCard();
        return ResponseEntity.ok(response);
    }
}