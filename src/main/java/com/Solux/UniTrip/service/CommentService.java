package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.dto.request.CommentCreateRequest;
import com.Solux.UniTrip.dto.response.CommentLikeResponse;
import com.Solux.UniTrip.dto.response.CommentResponse;
import com.Solux.UniTrip.dto.request.CommentUpdateRequest;
import com.Solux.UniTrip.dto.response.CommentUpdateResponse;
import com.Solux.UniTrip.entity.Comment;
import com.Solux.UniTrip.entity.CommentLikes;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.CommentLikeRepository;
import com.Solux.UniTrip.repository.CommentRepository;
import com.Solux.UniTrip.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;


    //댓글 생성
    public CommentResponse createComment(CommentCreateRequest dto, Long userId) {
        if (dto == null || dto.getPostId() == null || dto.getContent() == null ||
                dto.getContent().trim().isEmpty()) {
            throw new BaseException(FailureStatus._BAD_REQUSET);
        }

        // 실제로는 Post 존재 여부를 확인해야 함
        boolean postExists = true; // TODO: Post 존재 체크
        if (!postExists) {
            throw new BaseException(FailureStatus._POST_NOT_FOUND);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        Comment comment = new Comment(
                dto.getPostId(),
                userId,
                dto.getContent()
        );

        Comment saved = commentRepository.save(comment);

        return CommentResponse.builder()
                .commentId(saved.getCommentId())
                .postId(saved.getPostId())
                .content(saved.getContent())
                .author(user.getNickname())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    //댓글 수정
    @Transactional
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest dto, Long userId) {
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new BaseException(FailureStatus._BAD_REQUSET);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(FailureStatus._COMMENT_NOT_FOUND));

        if (!comment.getUserId().equals(userId)) {
            throw new BaseException(FailureStatus._UNAUTHORIZED);
        }

        comment.updateContent(dto.getContent());

        return CommentUpdateResponse.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(FailureStatus._COMMENT_NOT_FOUND));

        if (!comment.getUserId().equals(userId)) {
            throw new BaseException(FailureStatus._UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }

    //댓글 좋아요 등록/삭제
    @Transactional
    public CommentLikeResponse toggleLike(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(FailureStatus._COMMENT_NOT_FOUND));

        Optional<CommentLikes> existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

        boolean isLiked;
        if (existingLike.isPresent()) {
            commentLikeRepository.delete(existingLike.get());
            comment.setLikeCount(comment.getLikeCount() - 1);
            isLiked = false;
        } else {
            CommentLikes like = new CommentLikes(commentId, userId);
            commentLikeRepository.save(like);
            comment.setLikeCount(comment.getLikeCount() + 1);
            isLiked = true;
        }

        return CommentLikeResponse.builder()
                .commentId(commentId)
                .isLiked(isLiked)
                .likeCount(comment.getLikeCount())
                .build();
    }
}
