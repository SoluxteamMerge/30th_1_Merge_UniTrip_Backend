package com.Solux.UniTrip.comment.controller;
//댓글 controller
//백다현

import com.Solux.UniTrip.comment.dto.CommentCreateRequestDto;
import com.Solux.UniTrip.comment.dto.CommentResponseDto;
import com.Solux.UniTrip.comment.dto.CommentUpdateRequestDto;
import com.Solux.UniTrip.comment.dto.CommentUpdateResponseDto;
import com.Solux.UniTrip.comment.service.CommentService;
import com.Solux.UniTrip.common.dto.ApiSuccessResponse;
import com.Solux.UniTrip.common.dto.PageResponseDto;
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

    // 댓글 목록 조회 API
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<PageResponseDto>> getComments(
            // TODO: postId를 주입받으려면 아래 주석 해제
            //@RequestParam Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long postId = 1L; // 임시값
        PageResponseDto response = commentService.getComments(postId, page, size);

        return ResponseEntity.ok(
                new ApiSuccessResponse<>(200, "댓글 목록 조회에 성공하였습니다.", response)
        );
    }
}
