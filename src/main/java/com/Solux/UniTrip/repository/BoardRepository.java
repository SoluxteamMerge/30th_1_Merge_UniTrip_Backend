package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUser(User user);
}
