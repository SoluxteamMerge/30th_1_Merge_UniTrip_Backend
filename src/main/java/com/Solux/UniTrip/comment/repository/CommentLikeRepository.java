package com.Solux.UniTrip.comment.repository;
//댓글 좋아요 레포지토리
//백다현

import com.Solux.UniTrip.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);
    Long countByCommentId(Long commentId);
}
