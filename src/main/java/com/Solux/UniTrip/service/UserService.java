package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.exception.UploadFailureExpection;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.UserProfileModifyRequest;
import com.Solux.UniTrip.dto.request.UserProfileRequest;
import com.Solux.UniTrip.dto.response.*;
import com.Solux.UniTrip.entity.Board;
import com.Solux.UniTrip.entity.Scrap;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.ScrapRepository;
import com.Solux.UniTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ScrapRepository scrapRepository;
    private final JavaMailSender mailSender;
    private final S3Uploader s3Uploader;

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
    public boolean registerProfile(String token, UserProfileRequest request) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        // 닉네임이 이미 등록된 경우
        if (!user.getNickname().equals("defaultNickname")) {
            return false;
        }

        //user 엔티티에 변경 상태 업데이트 요철
        user.updateProfile(
                request.getNickname(),
                request.getPhoneNumber(),
                User.UserType.valueOf(request.getUserType().toUpperCase()),
                request.isEmailVerified()
        );
        return true;
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

    // 사용자 정보 조회
    @Transactional
    public UserProfileResponse getProfile(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));
        return new UserProfileResponse(user.getName(), user.getNickname(), user.getPhoneNumber(), user.getUserType(), user.isEmailVerified());
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
        if (user.getNickname().equals("defaultNickname")) {
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

    //마이페이지 사용자 정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException(("Invalid Token"));
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));
        return new UserInfoResponse(user.getNickname(), user.getProfileImageUrl());
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

    //  이메일 인증 코드 저장소 (임시 Map)
    private final Map<String, VerificationInfo> verificationStorage = new ConcurrentHashMap<>();
    private static final int CODE_EXPIRE_MINUTES = 3;

    //인증 메일 전송
    public void sendVerificationEmail(String email) {
        // 1. 도메인 체크
        if (!email.endsWith("@sookmyung.ac.kr")) {
            throw new BaseException(FailureStatus.INVALID_EMAIL_DOMAIN);
        }

        // 2. 인증 코드 생성
        String code = generateVerificationCode();

        // 3. 이메일 전송
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[UniTrip] 이메일 인증 코드 안내");
            message.setText("인증 코드는 [" + code + "] 입니다. 3분 이내에 입력해주세요.");
            mailSender.send(message);
        } catch (Exception e) {
            throw new BaseException(FailureStatus.EMAIL_SEND_FAILED);
        }

        // 4. 인증 정보 저장
        verificationStorage.put(email, new VerificationInfo(code, LocalDateTime.now()));
    }

    //인증 코드 검증
    public void verifyEmailCode(String email, String code) {
        VerificationInfo info = verificationStorage.get(email);

        if (info == null) {
            throw new BaseException(FailureStatus.VERIFICATION_NOT_FOUND);
        }

        // 만료 체크
        if (info.createdAt.plusMinutes(CODE_EXPIRE_MINUTES).isBefore(LocalDateTime.now())) {
            verificationStorage.remove(email);
            throw new BaseException(FailureStatus.VERIFICATION_EXPIRED);
        }

        // 코드 불일치
        if (!info.code.equals(code)) {
            throw new BaseException(FailureStatus.VERIFICATION_CODE_MISMATCH);
        }

        // 인증 성공 처리
        verificationStorage.remove(email);

        // 이메일 인증된 사용자 처리
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        user.verifyEmail();
        userRepository.save(user);
    }

    //인증 코드 생성
    private String generateVerificationCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    //인증 코드 저장 객체
    private static class VerificationInfo {
        private final String code;
        private final LocalDateTime createdAt;

        public VerificationInfo(String code, LocalDateTime createdAt) {
            this.code = code;
            this.createdAt = createdAt;
        }
    }

    // 프로필 사진 업로드
    @Transactional
    public ProfileImageResponse uploadProfileImage(String token, MultipartFile imagefile) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid Token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        // S3 에 이미지 업로드
        String imageUrl = s3Uploader.uploadProfileImage(imagefile, "profile");

        // 사용자 프로필 이미지 url 저장
        user.updateProfileImage(imageUrl);
        userRepository.save(user);

        return new ProfileImageResponse(imageUrl);
    }

    //프로필 사진 삭제
    @Transactional
    public void deleteProfileImage(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid Token");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        String imageUrl = user.getProfileImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            s3Uploader.deleteFile(imageUrl);

            // DB에서 프로필 이미지 URL 삭제
            user.updateProfileImage(null);
            userRepository.save(user);
        } else {
            throw new UploadFailureExpection(FailureStatus._PROFILEIMAGE_NOT_FOUND);
        }
    }


}
