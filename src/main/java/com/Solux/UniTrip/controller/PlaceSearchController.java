package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.dto.response.PlaceSearchResponse;
import com.Solux.UniTrip.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place/")
public class PlaceSearchController {

    private final PlaceSearchService placeSearchService;

    ///api/place/search 로 들어오는 요청 처리
    //RequestParam은 요청으로 들어오는 쿼리 파라미터를 받음
    //결과는 PlaceSearchResponse 의 List 형태로 반환
    @GetMapping("/search")
    public List<PlaceSearchResponse> searchplace(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String token
            // 현재 인증된 사용자 정보를 가져와서 인증된 사용자만 api에 접근하도록
            ) {
        //bearer 접두사 제거
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        //토큰을 서비스에 넘겨서 인증 검증
        List<PlaceSearchResponse> results = placeSearchService.searchPlaces(keyword, accessToken);
        return results;
    }
}



