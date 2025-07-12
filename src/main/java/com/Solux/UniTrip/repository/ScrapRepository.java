package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Scrap;
import com.Solux.UniTrip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByUserAndPost(User user, Board board);
    boolean existsByUserAndBoard(User user, Board board);
    List<Scrap> findAllByUser(User user);
    long countByBoard(Board board);
}
