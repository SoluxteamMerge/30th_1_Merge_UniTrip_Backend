package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.PostLikes;
import com.Solux.UniTrip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikesRepository extends JpaRepository<PostLikes, Long> {
    Optional<PostLikes> findByBoardAndUserAndStatus(Board board, User user, boolean status);
    Long countByBoardAndStatus(Board board, boolean status);
}
