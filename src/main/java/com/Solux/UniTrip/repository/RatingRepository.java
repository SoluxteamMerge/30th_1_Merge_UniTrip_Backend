package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Rating;
import com.Solux.UniTrip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByBoardAndUser(Board board, User user);
}

