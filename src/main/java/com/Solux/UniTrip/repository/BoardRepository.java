package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUser(User user);

    List<Board> findByBoardType(BoardType boardType);
    //List<Board> findByCategory(PostCategory category);
    @Query("SELECT DISTINCT b FROM Board b JOIN b.category c " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.placeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Board> searchByKeyword(@Param("keyword") String keyword);
    List<Board> findByPlace_Region(Place.Region region);







}
