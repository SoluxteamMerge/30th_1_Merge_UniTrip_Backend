package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
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

    public void deleteUser(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));
        userRepository.delete(user);
    }

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
