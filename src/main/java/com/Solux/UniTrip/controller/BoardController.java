package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.dto.request.BoardRequest;
import com.Solux.UniTrip.dto.request.RatingRequest;
import com.Solux.UniTrip.dto.response.*;
import com.Solux.UniTrip.entity.BoardType;
import com.Solux.UniTrip.entity.Place;
import com.Solux.UniTrip.repository.BoardRepository;
import com.Solux.UniTrip.service.BoardService;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.Solux.UniTrip.entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final KeywordService keywordService;
    private final BoardRepository boardRepository;

    // 리뷰 작성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardResponse> createBoard(
            @RequestPart BoardRequest request,
            @AuthenticationPrincipal User user,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ) {
        BoardResponse response = boardService.createBoard(request, user, images);
        return ResponseEntity.ok(response);
    }

    // 리뷰 조회(청춘카드)
    @GetMapping
    public ApiResponse<List<ReviewResultResponse>> getAllReviews(@AuthenticationPrincipal User user) {
        List<ReviewResultResponse> allReview =  boardService.getAllCard(user);
        return ApiResponse.onSuccess(allReview, SuccessStatus._GET_ALL_REVIEWS_SUCCESS);
    }

    // 리뷰 조회(게시판별)
    @GetMapping(params = "boardType")
    public ResponseEntity<BoardListResponse> getBoardsByType(@RequestParam BoardType boardType, @AuthenticationPrincipal User user) {
        BoardListResponse response = boardService.getCardsByBoardType(boardType, user);
        return ResponseEntity.ok(response);
    }

    // 리뷰 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<BoardItemResponse> getBoardById(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        BoardItemResponse response = boardService.getBoardById(postId, user, true);
        return ResponseEntity.ok(response);
    }

    // 리뷰 추천 조회
    @GetMapping("/recommend")
    public ResponseEntity<BoardItemResponse> getRecommendBoard() {
        Long randomId = boardRepository.findRandomPostId();  // ← 여기서 랜덤 postId 가져옴
        BoardItemResponse response = boardService.getBoardById(randomId, null, false);
        return ResponseEntity.ok(response);
    }

    // 리뷰 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long postId,
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal User user, @RequestPart(value = "images", required = false) List<MultipartFile> images

            ) {
        BoardResponse response = boardService.updateBoard(postId, request, user, images);
        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<BoardResponse> deleteBoard(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        BoardResponse response = boardService.deleteBoard(postId, user);
        return ResponseEntity.ok(response);
    }
    
    // 리뷰 검색 결과 조회
    @GetMapping("/search")
    public ApiResponse<List<ReviewResultResponse>> searchResults(
            @RequestHeader ("Authorization") String token,
            @RequestParam String keyword
    ) {
        // 검색 키워드 카운트 증가
        keywordService.increaseSearchCount(keyword);

        List<ReviewResultResponse> searchs = boardService.searchResults(keyword, token);
        return ApiResponse.onSuccess(searchs, SuccessStatus._REVIEW_SEARCH_SUCCESS);
    }

    // 인기 키워드로 검색 결과 조회
    @GetMapping("/popular")
    public ApiResponse<List<ReviewResultResponse>> searchPopularKeywords(
            @RequestHeader("Authorization") String token,
            @RequestParam String keyword
    ){
        List<ReviewResultResponse> searchs = boardService.searchResults(keyword, token);
        return ApiResponse.onSuccess(searchs, SuccessStatus._POPULAR_SEARCH_SUCCESS);
    }

    // 장소별 필터링 결과 조회
    @GetMapping("/filter")
    public ApiResponse<List<ReviewResultResponse>> getPlacesByRegion(
            @RequestHeader("Authorization") String token,
            @RequestParam Place.Region region
    ){
        List<ReviewResultResponse> places = boardService.getPlacesByRegion(region, token);
        return ApiResponse.onSuccess(places, SuccessStatus._PLACE_FILTERING_SUCCESS);
    }

    //리뷰 별점 등록/삭제
    @PostMapping("/{postId}/rating")
    public ResponseEntity<ApiResponse<RatingResponse>> toggleRating(
            @PathVariable Long postId,
            @RequestBody RatingRequest requestDto,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new BaseException(FailureStatus._UNAUTHORIZED);
        }

        RatingResponse responseDto = boardService.toggleRating(postId, user.getUserId(),
                requestDto.getRating());

        SuccessStatus status = responseDto.isRated() ?
                SuccessStatus._RATING_REGISTERED :
                SuccessStatus._RATING_DELETED;

        return ResponseEntity.ok(ApiResponse.onSuccess(responseDto, status));
    }
}