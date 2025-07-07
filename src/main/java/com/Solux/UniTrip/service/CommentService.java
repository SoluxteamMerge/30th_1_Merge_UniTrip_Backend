package com.Solux.UniTrip.service;

<<<<<<< Updated upstream
import com.Solux.UniTrip.dto.*;
import com.Solux.UniTrip.entity.Comment;
import com.Solux.UniTrip.entity.CommentLike;
import com.Solux.UniTrip.repository.CommentLikeRepository;
import com.Solux.UniTrip.repository.CommentRepository;
import com.Solux.UniTrip.common.exception.NotFoundException;
import com.Solux.UniTrip.common.exception.BadRequestException;
=======
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
>>>>>>> Stashed changes
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
<<<<<<< Updated upstream

    public CommentResponseDto createComment(CommentCreateRequestDto dto) {
=======
    private final UserRepository userRepository;

    public CommentResponse createComment(CommentCreateRequest dto, Long userId) {
>>>>>>> Stashed changes
        if (dto == null || dto.getPostId() == null || dto.getContent() == null ||
                dto.getContent().trim().isEmpty()) {
            throw new BadRequestException("요청 값이 올바르지 않습니다.");
        }

        // 실제로는 Post 존재 여부를 확인해야 함 (PostRepository.findById)
        boolean postExists = true; // TODO: Post 존재 체크
        if (!postExists) {
            throw new NotFoundException("해당 게시글을 찾을 수 없습니다.");
        }

        // userId로 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        Comment comment = new Comment(
                dto.getPostId(),
                userId,
                dto.getContent()
        );

        Comment saved = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .commentId(saved.getCommentId())
                .postId(saved.getPostId())
                .content(saved.getContent())
                .author(user.getNickname())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // 댓글 수정
    @Transactional
<<<<<<< Updated upstream
    public CommentUpdateResponseDto updateComment(Long commentId, CommentUpdateRequestDto dto) {
=======
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest dto, Long userId) {
>>>>>>> Stashed changes
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new BadRequestException("요청 값이 올바르지 않습니다.");
        }

        //댓글 존재 하지 않을 때
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        //현재 유저와 작성자 비교해서 권한 체크
        if (!comment.getUserId().equals(userId)) {
            throw new BaseException(FailureStatus._UNAUTHORIZED);
        }

        comment.updateContent(dto.getContent());

        return CommentUpdateResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        //현재 유저와 작성자 비교해서 권한 체크
        if (!comment.getUserId().equals(userId)) {
            throw new BaseException(FailureStatus._UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }

<<<<<<< Updated upstream
    //댓글 좋아요
    @Transactional
    public CommentLikeResponseDto toggleLike(Long commentId) {
        // 실제로는 현재 로그인 유저 ID를 가져와야 합니다.
        // 예) Long userId = SecurityContextHolder.getContext() ...
        Long userId = 1L; // TODO: 인증 구현 후 현재 사용자 ID로 대체

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        Optional<CommentLike> existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

        boolean isLiked;
        if (existingLike.isPresent()) {
            // 좋아요 취소
            commentLikeRepository.delete(existingLike.get());
            //좋아요 취소 시 카운트를 내림
            comment.setLikeCount(comment.getLikeCount() - 1);
            isLiked = false;
        }

        else {
            // 좋아요 등록
            CommentLike like = new CommentLike(commentId, userId);
            commentLikeRepository.save(like);
            //좋아요 등록 시 카운트 올림
=======
    //댓글 좋아요 토글
    @Transactional
    public CommentLikeResponse toggleLike(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(FailureStatus._COMMENT_NOT_FOUND));

        Optional<CommentLikes> existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

        boolean isLiked;
        if (existingLike.isPresent()) {

            // 좋아요 취소
            commentLikeRepository.delete(existingLike.get());
            comment.setLikeCount(comment.getLikeCount() - 1);
            isLiked = false;
        } else {
            // 좋아요 등록
            CommentLikes like = new CommentLikes(commentId, userId);
            commentLikeRepository.save(like);
>>>>>>> Stashed changes
            comment.setLikeCount(comment.getLikeCount() + 1);
            isLiked = true;
        }

<<<<<<< Updated upstream
        return CommentLikeResponseDto.builder()
=======
        return CommentLikeResponse.builder()
>>>>>>> Stashed changes
                .commentId(commentId)
                .isLiked(isLiked)
                .likeCount(comment.getLikeCount())
                .build();
    }

}