package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.dto.request.CommentCreateRequest;
import com.Solux.UniTrip.dto.response.CommentLikeResponse;
import com.Solux.UniTrip.dto.response.CommentResponse;
import com.Solux.UniTrip.dto.request.CommentUpdateRequest;
import com.Solux.UniTrip.dto.response.CommentUpdateResponse;
import com.Solux.UniTrip.service.CommentService;
import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성 API
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @RequestBody CommentCreateRequest request
    ) {
        CommentResponse responseDto = commentService.createComment(request);
        return ResponseEntity.status(201).body(
                ApiResponse.onSuccess(responseDto, SuccessStatus._OK)
        );
    }

    // 댓글 수정 API
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        CommentUpdateResponse response = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(response, SuccessStatus._OK)
        );
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(null, SuccessStatus._OK)
        );
    }

    // 댓글 좋아요 등록/삭제 API
    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiResponse<CommentLikeResponse>> toggleLike(
            @PathVariable Long commentId
    ) {
        CommentLikeResponse response = commentService.toggleLike(commentId);

        // 좋아요 상태에 따라 메시지 달리하고 싶다면 SuccessStatus 대신 아래처럼 of() 팩토리 메서드 사용
        String message = response.isLiked()
                ? "댓글에 좋아요를 등록하였습니다."
                : "댓글에 좋아요를 취소하였습니다.";

        return ResponseEntity.ok(
                ApiResponse.of(response, message, 200)
        );
    }

}
