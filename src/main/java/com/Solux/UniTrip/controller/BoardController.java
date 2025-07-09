package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.response.BoardResponse;
import com.Solux.UniTrip.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.Solux.UniTrip.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal User user) {
        BoardResponse response = boardService.createBoard(request, user);
        return ResponseEntity.ok(response);
    }
}