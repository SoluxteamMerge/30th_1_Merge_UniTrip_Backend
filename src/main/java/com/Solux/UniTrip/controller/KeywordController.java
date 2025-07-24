package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.ForbiddenException;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.dto.response.PopularKeywordResponse;
import com.Solux.UniTrip.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularKeywords(
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (limit <= 0) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 400);
            error.put("message", "요청 값이 올바르지 않습니다.");
            error.put("data", null);
            return ResponseEntity.badRequest().body(error);
        }

        List<PopularKeywordResponse> result = keywordService.getPopularKeywords(limit);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 200);
        response.put("message", "인기 키워드 목록 조회에 성공하였습니다.");
        response.put("data", result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/rank")
    public ApiResponse<Void> updateKeywordRank() {
        try {
            keywordService.updateKeywordRanking();
            return ApiResponse.onSuccess(null, SuccessStatus._OK); // 200 OK

        } catch (ForbiddenException e) {
            return ApiResponse.onFailure(null, e.getFailureStatus()); // 403 Forbidden

        } catch (Exception e) {
            return ApiResponse.of(null, "서버 내부 오류가 발생하였습니다.", 500); // 500 Internal Server Error
        }
    }

}
