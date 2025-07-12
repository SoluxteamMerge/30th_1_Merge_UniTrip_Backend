package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.dto.request.UserProfileModifyRequest;
import com.Solux.UniTrip.dto.request.UserProfileRequest;
import com.Solux.UniTrip.dto.response.UserInfoResponse;
import com.Solux.UniTrip.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<UserInfoResponse> getUserInfo(
            @RequestHeader("Authorization") String token
    ) {
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        UserInfoResponse userInfo = userService.getUserInfo(accessToken);
        return ApiResponse.onSuccess(userInfo, SuccessStatus._GET_USER_INFO_SUCCESS);
    }

    @PostMapping("/profile")
    public ApiResponse<?> registerProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserProfileRequest request
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        userService.registerProfile(accessToken, request);
        return ApiResponse.onSuccess(null, SuccessStatus._USER_PROFILE_REGISTERED);
    }

    @PatchMapping("/modify")
    public ApiResponse<?> modifyProfile(
            @RequestHeader("Authorization") String token,
            @Valid@RequestBody UserProfileModifyRequest request
    ){
        String accessToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        userService.modifyProfile(accessToken, request);
        return ApiResponse.onSuccess(null, SuccessStatus._MODIFY_USER_INFO_SUCCESS);
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
