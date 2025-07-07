package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.CommentCreateRequest;
import com.Solux.UniTrip.dto.response.CommentLikeResponse;
import com.Solux.UniTrip.dto.response.CommentResponse;
import com.Solux.UniTrip.dto.request.CommentUpdateRequest;
import com.Solux.UniTrip.dto.response.CommentUpdateResponse;
import com.Solux.UniTrip.repository.UserRepository;
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
        public ResponseEntity<ApiResponse<CommentResponse>> createComment(
                @RequestHeader("Authorization") String authorizationHeader,
                @RequestBody CommentCreateRequest request
    )
    {
            Long userId = getUserIdFromHeader(authorizationHeader);
            CommentResponse responseDto = commentService.createComment(request, userId);
            return ResponseEntity.status(201).body(
                    ApiResponse.onSuccess(responseDto, SuccessStatus._OK)
            );
        }

        // 댓글 수정 API
        @PatchMapping("/{commentId}")

        public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(
                @RequestHeader("Authorization") String authorizationHeader,

        @PathVariable Long commentId,
        @RequestBody CommentUpdateRequest request
    )
        {
            Long userId = getUserIdFromHeader(authorizationHeader);
            CommentUpdateResponse response = commentService.updateComment(commentId, request, userId);

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

            public ResponseEntity<ApiResponse<CommentLikeResponse>> toggleLike(
                    @RequestHeader("Authorization") String authorizationHeader,
                    @PathVariable Long commentId
    ) {
                Long userId = getUserIdFromHeader(authorizationHeader);
                CommentLikeResponse response = commentService.toggleLike(commentId, userId);

                String message = response.isLiked()
                        ? "댓글에 좋아요를 등록하였습니다."
                        : "댓글에 좋아요를 취소하였습니다.";

                return ResponseEntity.ok(
                        ApiResponse.of(response, message, 200)
                );
            }

            // 공통 로직
            private Long getUserIdFromHeader(String authorizationHeader) {
                //  Bearer 접두사 제거
                String token = authorizationHeader.startsWith("Bearer ")
                        ? authorizationHeader.substring(7).trim()
                        : authorizationHeader.trim();
                String email = jwtTokenProvider.getEmailFromToken(token);
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND))
                        .getUserId();
            }
        }