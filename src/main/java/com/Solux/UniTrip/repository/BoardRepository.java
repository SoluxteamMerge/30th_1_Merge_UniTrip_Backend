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


    List<Board> findByPlace_Region(Place.Region region);

    @Query(value = "SELECT post_id FROM board ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Long findRandomPostId();

    // 기본 검색
    @Query("SELECT DISTINCT b FROM Board b LEFT JOIN b.category c " +
            "LEFT JOIN b.place p " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.placeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Board> searchByKeyword(@Param("keyword") String keyword);

    // 인기순 정렬
    @Query("SELECT DISTINCT b FROM Board b LEFT JOIN b.category c " +
            "LEFT JOIN b.place p " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.placeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.likeCount DESC")
    List<Board> searchByKeywordOrderByLikesDesc(@Param("keyword") String keyword);

    // 최신순 정렬
    @Query("SELECT DISTINCT b FROM Board b LEFT JOIN b.category c " +
            "LEFT JOIN b.place p " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.placeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.createdAt DESC")
    List<Board> searchByKeywordOrderByCreatedAtDesc(@Param("keyword") String keyword);

}
