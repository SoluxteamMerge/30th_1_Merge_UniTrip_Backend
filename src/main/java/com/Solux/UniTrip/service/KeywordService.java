package com.Solux.UniTrip.service;

import com.Solux.UniTrip.dto.response.PopularKeywordResponse;
import com.Solux.UniTrip.entity.KeywordRanking;
import com.Solux.UniTrip.entity.SearchKeyword;
import com.Solux.UniTrip.repository.KeywordRankingRepository;
import com.Solux.UniTrip.repository.SearchKeywordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRankingRepository keywordRankingRepository;
    private final SearchKeywordRepository searchKeywordRepository;

    //인기 키워드 목록 조회
    public List<PopularKeywordResponse> getPopularKeywords(int limit) {
        List<Object[]> rawResults = keywordRankingRepository.findPopularKeywords(limit);

        return rawResults.stream()
                .map(row -> new PopularKeywordResponse(
                        (String) row[0],
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).intValue()
                ))
                .collect(Collectors.toList());
    }

    //인기 키워드 랭킹 갱신
    @Transactional
    public void updateKeywordRanking() {
        // 기존 랭킹 초기화
        keywordRankingRepository.deleteAll();

        // 인기 검색어 기준 내림차순 정렬 후 상위 N개 추출 (예: 상위 10개)
        List<SearchKeyword> topKeywords = searchKeywordRepository.findTop10ByOrderBySearchCountDesc();

        // 새 랭킹 저장
        int rank = 1;
        for (SearchKeyword keyword : topKeywords) {
            keywordRankingRepository.save(
                    KeywordRanking.builder()
                            .keywordId((long) rank) // keyword_id가 PK
                            .keyword(keyword.getKeyword())
                            .rank(rank++)
                            .calculatedAt(LocalDateTime.now())
                            .build()
            );

        }
    }
}
