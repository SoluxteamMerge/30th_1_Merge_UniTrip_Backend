package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Scrap;
import com.Solux.UniTrip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardScarpRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByBoardAndUser(Board board, User user);
    Long countByBoard(Board board);
}
