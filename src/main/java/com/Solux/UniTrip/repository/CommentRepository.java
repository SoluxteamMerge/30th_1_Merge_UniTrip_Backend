package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}