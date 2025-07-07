package com.Solux.UniTrip.controller;

<<<<<<< Updated upstream
import com.Solux.UniTrip.dto.*;
=======
import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.CommentCreateRequest;
import com.Solux.UniTrip.dto.response.CommentLikeResponse;
import com.Solux.UniTrip.dto.response.CommentResponse;
import com.Solux.UniTrip.dto.request.CommentUpdateRequest;
import com.Solux.UniTrip.dto.response.CommentUpdateResponse;
import com.Solux.UniTrip.repository.UserRepository;
>>>>>>> Stashed changes
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
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 댓글 생성 API
    @PostMapping
<<<<<<< Updated upstream
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @RequestBody CommentCreateRequestDto request
    ) {
        CommentResponseDto responseDto = commentService.createComment(request);
=======
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CommentCreateRequest request
    ) {
        Long userId = getUserIdFromHeader(authorizationHeader);
        CommentResponse responseDto = commentService.createComment(request, userId);
>>>>>>> Stashed changes
        return ResponseEntity.status(201).body(
                ApiResponse.onSuccess(responseDto, SuccessStatus._OK)
        );
    }

    // 댓글 수정 API
    @PatchMapping("/{commentId}")
<<<<<<< Updated upstream
    public ResponseEntity<ApiResponse<CommentUpdateResponseDto>> updateComment(
=======
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(
            @RequestHeader("Authorization") String authorizationHeader,
>>>>>>> Stashed changes
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto request
    ) {
<<<<<<< Updated upstream
        CommentUpdateResponseDto response = commentService.updateComment(commentId, request);
=======
        Long userId = getUserIdFromHeader(authorizationHeader);
        CommentUpdateResponse response = commentService.updateComment(commentId, request, userId);
>>>>>>> Stashed changes
        return ResponseEntity.ok(
                ApiResponse.onSuccess(response, SuccessStatus._OK)
        );
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long commentId
    ) {
        Long userId = getUserIdFromHeader(authorizationHeader);
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(null, SuccessStatus._OK)
        );
    }

    // 댓글 좋아요 등록/삭제 API
    @PostMapping("/{commentId}/like")
<<<<<<< Updated upstream
    public ResponseEntity<ApiResponse<CommentLikeResponseDto>> toggleLike(
            @PathVariable Long commentId
    ) {
        CommentLikeResponseDto response = commentService.toggleLike(commentId);
=======
    public ResponseEntity<ApiResponse<CommentLikeResponse>> toggleLike(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long commentId
    ) {
        Long userId = getUserIdFromHeader(authorizationHeader);
        CommentLikeResponse response = commentService.toggleLike(commentId, userId);
>>>>>>> Stashed changes

        String message = response.isLiked()
                ? "댓글에 좋아요를 등록하였습니다."
                : "댓글에 좋아요를 취소하였습니다.";

        return ResponseEntity.ok(
                ApiResponse.of(response, message, 200)
        );
    }

    // 공통 로직
    private Long getUserIdFromHeader(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND))
                .getUserId();
    }
}
