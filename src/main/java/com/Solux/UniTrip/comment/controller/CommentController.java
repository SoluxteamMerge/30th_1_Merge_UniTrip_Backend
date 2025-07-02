package com.Solux.UniTrip.comment.controller;
//댓글 controller
//백다현

import com.Solux.UniTrip.comment.dto.*;
import com.Solux.UniTrip.comment.service.CommentService;
import com.Solux.UniTrip.common.dto.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    //댓글 생성 API
    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestBody CommentCreateRequestDto request
    ) {
        CommentResponseDto responseDto = commentService.createComment(request);
        return ResponseEntity.status(201).body(
                new ApiSuccessResponse<>(201, "댓글이 성공적으로 작성되었습니다.",
                        responseDto)
        );
    }

    //댓글 수정 API
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiSuccessResponse<CommentUpdateResponseDto>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto request
    ) {
        CommentUpdateResponseDto response = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(200, "댓글이 성공적으로 수정되었습니다.", response)
        );
    }

    //댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(200, "댓글이 성공적으로 삭제되었습니다.", null)
        );
    }

    //댓글 좋아요 등록/삭제 API
    @PostMapping("/{commentId}/like")
    public ResponseEntity<ApiSuccessResponse<CommentLikeResponseDto>> toggleLike(
            @PathVariable Long commentId
    ) {
        CommentLikeResponseDto response = commentService.toggleLike(commentId);

        String message = response.isLiked()
                ? "댓글에 좋아요를 등록하였습니다."
                : "댓글에 좋아요를 취소하였습니다.";

        return ResponseEntity.ok(
                new ApiSuccessResponse<>(200, message, response)
        );
    }

}