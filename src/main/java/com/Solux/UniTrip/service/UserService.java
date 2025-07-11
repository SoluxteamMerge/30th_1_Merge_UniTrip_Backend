package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.UserProfileRequest;
import com.Solux.UniTrip.dto.response.UserInfoResponse;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //회원 탈퇴
    public void deleteUser(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));
        userRepository.delete(user);
    }

    //사용자 정보 등록
    @Transactional
    public void registerProfile(String token, UserProfileRequest request) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        //user 엔티티에 변경 상태 업데이트 요철
        user.updateProfile(
                request.getNickname(),
                request.getPhoneNumber(),
                User.UserType.valueOf(request.getUserType().toUpperCase()),
                request.isEmailVerified()
        );
    }

    //사용자 정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));
        return new UserInfoResponse(user.getName(), user.getNickname(), user.getProfileImageUrl());
    }
}
