package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.dto.request.CommentCreateRequest;
import com.Solux.UniTrip.dto.response.CommentResponse;
import com.Solux.UniTrip.dto.request.CommentUpdateRequest;
import com.Solux.UniTrip.dto.response.CommentUpdateResponse;
import com.Solux.UniTrip.entity.Comment;
import com.Solux.UniTrip.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponse createComment(CommentCreateRequest dto) {
        if (dto == null || dto.getPostId() == null || dto.getContent() == null ||
                dto.getContent().trim().isEmpty()) {
            throw new BaseException(FailureStatus._BAD_REQUSET);
        }


        // 실제로는 Post 존재 여부를 확인해야 함 (PostRepository.findById)
        boolean postExists = true; // TODO: Post 존재 체크
        if (!postExists) {
            throw new BaseException(FailureStatus._POST_NOT_FOUND);
        }

        Comment comment = new Comment(
                dto.getPostId(),
                null, // 로그인 구현 후 userId 설정
                dto.getContent()
        );

        Comment saved = commentRepository.save(comment);

        return CommentResponse.builder()
                .commentId(saved.getCommentId())
                .postId(saved.getPostId())
                .content(saved.getContent())
                .author("작성자") // 로그인 개발 후 유저 이름
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 댓글 수정
    @Transactional
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest dto) {
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new BaseException(FailureStatus._BAD_REQUSET);
        }


        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(FailureStatus._COMMENT_NOT_FOUND));

        // (추후) 작성자 권한 체크 필요

        comment.updateContent(dto.getContent());

        return CommentUpdateResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(FailureStatus._COMMENT_NOT_FOUND));

        // (추후) 현재 유저와 작성자 비교해서 권한 체크 필요
        commentRepository.delete(comment);
    }
}