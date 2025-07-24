package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, String> {
    List<SearchKeyword> findTop10ByOrderBySearchCountDesc();
}
