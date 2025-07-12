package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.UserProfileModifyRequest;
import com.Solux.UniTrip.dto.request.UserProfileRequest;
import com.Solux.UniTrip.dto.response.ReviewResultResponse;
import com.Solux.UniTrip.dto.response.ScrapResponse;
import com.Solux.UniTrip.dto.response.UserInfoResponse;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Scrap;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.ScrapRepository;
import com.Solux.UniTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ScrapRepository scrapRepository;

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

        //이미 등록된 닉네임
        if (user.getNickname() != null) {
            throw new BaseException(FailureStatus._PROFILE_ALREADY_REGISTERED);
        }

        //user 엔티티에 변경 상태 업데이트 요철
        user.updateProfile(
                request.getNickname(),
                request.getPhoneNumber(),
                User.UserType.valueOf(request.getUserType().toUpperCase()),
                request.isEmailVerified()
        );
    }

    //닉네임 중복 확인
    @Transactional
    public boolean checkNickname(String token, String nickname) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        //닉네임 중복 체크
        boolean isDuplicated = userRepository.existsByNickname(nickname);
        return isDuplicated;
    }

    //사용자 정보 수정
    @Transactional
    public void modifyProfile(String token, UserProfileModifyRequest request) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        //닉네임이 없는 경우 : 프로필이 등록되지 않은 사용자
        if (user.getNickname() == null) {
            throw new BaseException(FailureStatus._PROFILE_NOT_REGISTERED);
        }

        //닉네임이 null이 아니면 수정
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        //핸드폰 번호는 null 이어도 수정 가능
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        //유저 타입이 null이 아니면 수정
        if (request.getUserType() != null) {
            try {
                user.setUserType(User.UserType.valueOf(request.getUserType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BaseException(FailureStatus._INVALID_USER_TYPE);
            }
        }

        //이메일 인증이 1이면 수정
        if (request.isEmailVerified() != false) {
            user.setEmailVerified(request.isEmailVerified());
        }
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

    //내가 쓴 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResultResponse> getReviews(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid Token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        //리뷰 목록 가져오기
        List<Board> reviews = user.getBoardList();

        return reviews.stream()
                .map(review -> ReviewResultResponse.from(review))
                .toList();
    }

    //스크랩한 리뷰 조회
    @Transactional(readOnly = true)
    public List<ScrapResponse> getScraps(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid Token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        //스크랩 목록 가져오기
        List<Scrap> scraps = scrapRepository.findAllByUser(user);

        return scraps.stream()
                .map(scrap -> ScrapResponse.from(scrap.getBoard()))
                .toList();
    }
}
