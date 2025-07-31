package com.Solux.UniTrip.common.apiPayload.status;

import com.Solux.UniTrip.common.apiPayload.base.Basecode;
import com.Solux.UniTrip.common.apiPayload.base.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FailureStatus implements Basecode {

    //enum 에서 상수는 필드 선언보다 위에 있어야 함.
    _BAD_REQUEST(400, "잘못된 요청입니다."),
    _USER_NOT_FOUND(400, "존재하지 않는 회원입니다."),
    _UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),
    _INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    _COMMENT_NOT_FOUND(404, "해당 댓글을 찾을 수 없습니다."),
    _POST_NOT_FOUND(404, "해당 게시글을 찾을 수 없습니다."),
    _SCHEDULE_CONFLICT(409, "동일 기간에 이미 일정이 존재합니다."),
    _INVALID_DATE_FORMAT(400, "잘못된 날짜 형식입니다."),
    _INVALID_DATE_RANGE(400, "시작일은 종료일보다 이후일 수 없습니다."),
    FORBIDDEN(403, "권한이 없는 사용자입니다."),
    SCHEDULE_NOT_FOUND(404, "해당 일정을 찾을 수 없습니다."),
    _PROFILE_ALREADY_REGISTERED(409, "이미 프로필 등록을 하셨습니다."),
    _PROFILE_NOT_REGISTERED(400, "아직 프로필 정보가 등록되지 않았습니다."),
    _INVALID_USER_TYPE(400, "UserType 이 유효하지 않습니다."),
    INVALID_EMAIL_DOMAIN(400, "올바른 학교 이메일이 아닙니다."),
    EMAIL_SEND_FAILED(500,  "인증 메일 전송에 실패했습니다."),
    VERIFICATION_NOT_FOUND(400,  "인증 요청이 없습니다."),
    VERIFICATION_EXPIRED(400,  "인증 코드가 만료되었습니다."),
    VERIFICATION_CODE_MISMATCH(400,  "인증 코드가 일치하지 않습니다."),
    _PROFILEIMAGE_UPLOAD_FAILURE(500, "프로필 이미지 업로드에 실패하였습니다."),
    _PROFILEIMAGE_NOT_FOUND(400, "삭제할 프로필 사진이 없습니다."),
    SCHEDULE_OVERLAP(409, "해당 기간에 이미 등록된 일정이 존재합니다."),
    RATING_INVALID(400, "별점은 0.5 단위로 입력되어야 합니다."),
    _INVALID_NICKNAME_FORMAT(400, "닉네임에 공백을 포함할 수 없습니다."),
    _RATING_INVALID(400, "별점은 0.5 단위로 입력되어야 합니다."),
    _BOARDTYPE_NOT_NULL(400, "BoardType must not be empty"),
    _INVAILD_BOARDTYPE(400, "Invalid BoardType"),
    _CATEGORY_NOT_NULL(400, "Category name must not be empty"),
    _CATEGORY_NOT_FOUND(400, "카테고리를 찾을 수 없습니다.");


    private final int code;
    private final String message;

    @Override
    public ResponseDTO getResponse() {
        return ResponseDTO.builder()
                .code(code)
                .message(message)
                .build();
    }
}

