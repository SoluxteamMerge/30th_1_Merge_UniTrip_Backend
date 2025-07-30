package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.dto.request.EmailRequest;
import com.Solux.UniTrip.dto.request.EmailVerifyRequest;
import com.Solux.UniTrip.dto.request.UserProfileModifyRequest;
import com.Solux.UniTrip.dto.request.UserProfileRequest;
import com.Solux.UniTrip.dto.response.*;
import com.Solux.UniTrip.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //마이페이지 정보 조회
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
        if (nickname == null || nickname.trim().isEmpty() || nickname.contains(" ")) {
            throw new BaseException(FailureStatus._INVALID_NICKNAME_FORMAT);
        }

        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        boolean isDuplicated = userService.checkNickname(accessToken, nickname);

        return ApiResponse.onSuccess(
                Map.of("isDuplicated", isDuplicated),
                isDuplicated ? SuccessStatus._NICKNAME_DUPLICATED : SuccessStatus._NICKNAME_AVAILABLE
        );

    }

    // 사용자 정보 조회
    @GetMapping("/getProfile")
    public ApiResponse<UserProfileResponse> getProfile(
            @RequestHeader("Authorization") String token
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        UserProfileResponse userprofile = userService.getProfile(accessToken);
        return ApiResponse.onSuccess(userprofile, SuccessStatus._GET_PROFILE_SUCCESS);
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

    //

    //내가 쓴 리뷰 조회
    @GetMapping("/reviews")
    public ApiResponse<List<ReviewResultResponse>> getReviews(
            @RequestHeader("Authorization") String token
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        List<ReviewResultResponse> reviews = userService.getReviews(accessToken);

        //아직 작성한 리뷰가 없는 경우
        if (reviews.isEmpty()) {
            return ApiResponse.onSuccess(null,SuccessStatus._REVIEW_LIST_EMPTY);
        }

        return ApiResponse.onSuccess(reviews, SuccessStatus._GET_REVIEWS_SUCCESS);
    }

    //스크랩한 리뷰 조회
    @GetMapping("/scraps")
    public ApiResponse<List<ScrapResponse>> getScraps(
            @RequestHeader("Authorization") String token
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        List<ScrapResponse> scraps = userService.getScraps(accessToken);

        //아직 스크랩한 리뷰가 없는 경우
        if (scraps.isEmpty()) {
            return ApiResponse.onSuccess(null,SuccessStatus._SCRAPS_LIST_EMPTY);
        }

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

    // 이메일 인증 코드 전송
    @PostMapping("/email")
    public ApiResponse<Void> sendVerificationEmail(@Valid @RequestBody EmailRequest request) {
        userService.sendVerificationEmail(request.getEmail());
        return ApiResponse.onSuccess(null, SuccessStatus._EMAIL_SENT_SUCCESS);
    }

    // 이메일 인증 코드 검증
    @PostMapping("/email/verify")
    public ApiResponse<Void> verifyEmailCode(@Valid @RequestBody EmailVerifyRequest request) {
        userService.verifyEmailCode(request.getEmail(), request.getCode());
        return ApiResponse.onSuccess(null, SuccessStatus._EMAIL_VERIFIED_SUCCESS);
    }

    // 프로필 이미지 업로드
    @PostMapping("profileImage")
    public ApiResponse<ProfileImageResponse> uploadProfileImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("image") MultipartFile imagefile
    ) {
        log.info("🔐 Authorization Header: {}", token);
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        log.info("🧪 JWT token after removing Bearer: {}", token);


        ProfileImageResponse profileImageUrl = userService.uploadProfileImage(token, imagefile);
        return ApiResponse.onSuccess(profileImageUrl, SuccessStatus._PROFILEIMAGE_UPLOAD_SUCCESS);
    }

    // 프로필 이미지 삭제
    @DeleteMapping("profileImage")
    public ApiResponse<?> deleteProfileImage(
            @RequestHeader("Authorization") String token
    ){
        userService.deleteProfileImage(token);
        return ApiResponse.onSuccess(null, SuccessStatus._PROFILEIMAGE_DELETE_SUCCESS);
    }


}
