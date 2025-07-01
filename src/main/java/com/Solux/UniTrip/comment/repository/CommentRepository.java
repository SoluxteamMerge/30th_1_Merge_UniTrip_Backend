package com.Solux.UniTrip.comment.repository;

import com.Solux.UniTrip.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}