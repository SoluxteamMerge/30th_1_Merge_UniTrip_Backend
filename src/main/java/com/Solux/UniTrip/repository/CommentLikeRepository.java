package com.Solux.UniTrip.repository;
//댓글 좋아요 레포지토리
//백다현

import com.Solux.UniTrip.entity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByCommentIdAndUserId(Long commentId, Long userId);
    Long countByCommentId(Long commentId);
}
