package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.response.BoardItemResponse;
import com.Solux.UniTrip.dto.response.BoardListResponse;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.entity.BoardType;
import com.Solux.UniTrip.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.Solux.UniTrip.entity.User;
import org.springframework.web.bind.annotation.*;

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

    // 리뷰 조회(게시판별)
    @GetMapping(params = "boardType")
    public ResponseEntity<BoardListResponse> getBoardsByType(@RequestParam BoardType boardType) {
        BoardListResponse response = boardService.getCardsByBoardType(boardType);
        return ResponseEntity.ok(response);
    }

    // 리뷰 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<BoardItemResponse> getBoardById(@PathVariable Long postId) {
        BoardItemResponse response = boardService.getBoardById(postId);
        return ResponseEntity.ok(response);
    }

    // 리뷰 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long postId,
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal User user) {
        BoardResponse response = boardService.updateBoard(postId, request, user);
        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<BoardResponse> deleteBoard(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        BoardResponse response = boardService.deleteBoard(postId, user);
        return ResponseEntity.ok(response);
    }
}