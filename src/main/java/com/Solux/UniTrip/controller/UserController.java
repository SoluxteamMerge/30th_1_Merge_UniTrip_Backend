package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.dto.request.UserProfileModifyRequest;
import com.Solux.UniTrip.dto.request.UserProfileRequest;
import com.Solux.UniTrip.dto.response.ReviewResultResponse;
import com.Solux.UniTrip.dto.response.ScrapResponse;
import com.Solux.UniTrip.dto.response.UserInfoResponse;
import com.Solux.UniTrip.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //사용자 정보 조회
    @GetMapping
    public ApiResponse<UserInfoResponse> getUserInfo(
            @RequestHeader("Authorization") String token
    ) {
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        UserInfoResponse userInfo = userService.getUserInfo(accessToken);
        return ApiResponse.onSuccess(userInfo, SuccessStatus._GET_USER_INFO_SUCCESS);
    }

    //사용자 정보 등록
    @PostMapping("/profile")
    public ApiResponse<?> registerProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserProfileRequest request
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        userService.registerProfile(accessToken, request);
        return ApiResponse.onSuccess(null, SuccessStatus._USER_PROFILE_REGISTERED);
    }

    //닉네임 중복 확인
    @GetMapping("/check")
    public ApiResponse<?> checkNickname(
            @RequestHeader("Authorization") String token,
            @RequestParam String nickname
    ) {
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        boolean isDuplicated = userService.checkNickname(accessToken, nickname);
        return ApiResponse.onSuccess(
                Map.of("isDuplicated", isDuplicated),
                isDuplicated ? SuccessStatus._NICKNAME_DUPLICATED : SuccessStatus._NICKNAME_AVAILABLE
        );

    }

    //사용자 정보 수정
    @PatchMapping("/modify")
    public ApiResponse<?> modifyProfile(
            @RequestHeader("Authorization") String token,
            @Valid@RequestBody UserProfileModifyRequest request
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        userService.modifyProfile(accessToken, request);
        return ApiResponse.onSuccess(null, SuccessStatus._MODIFY_USER_INFO_SUCCESS);
    }

    //내가 쓴 리뷰 조회
    @GetMapping("/reviews")
    public ApiResponse<List<ReviewResultResponse>> getReviews(
            @RequestHeader("Authorization") String token
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        List<ReviewResultResponse> reviews = userService.getReviews(accessToken);
        return ApiResponse.onSuccess(reviews, SuccessStatus._GET_REVIEWS_SUCCESS);
    }

    //스크랩한 리뷰 조회
    @GetMapping("/scraps")
    public ApiResponse<List<ScrapResponse>> getScraps(
            @RequestHeader("Authorization") String token
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        List<ScrapResponse> scraps = userService.getScraps(accessToken);
        return ApiResponse.onSuccess(scraps, SuccessStatus._GET_SCRAPS_SUCCESS);
    }

    //회원 탈퇴
    @DeleteMapping("/signout")
    public ApiResponse<?> deleteUser(
            @RequestHeader("Authorization") String token
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        userService.deleteUser(accessToken);
        return ApiResponse.onSuccess(null, SuccessStatus._USER_DELETED);
    }
}
