package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.KeywordRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface KeywordRankingRepository extends JpaRepository<KeywordRanking, Long> {

    @Query(value = """
    SELECT 
        `keyword` AS `keyword`, 
        `rank` AS `rank`,
        (SELECT search_count 
         FROM search_keywords 
         WHERE search_keywords.`keyword` = keyword_ranking.`keyword`) AS searchCount
    FROM keyword_ranking
    ORDER BY `rank` ASC
    LIMIT :limit
""", nativeQuery = true)
    List<Object[]> findPopularKeywords(@Param("limit") int limit);
}
