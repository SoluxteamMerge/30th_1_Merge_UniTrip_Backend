package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.BoardType;
import com.Solux.UniTrip.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Integer> {

    Optional<PostCategory> findByBoardTypeAndCategoryName(BoardType boardType, String categoryName);

}