package com.Solux.UniTrip.common.apiPayload.status;

import com.Solux.UniTrip.common.apiPayload.base.Basecode;
import com.Solux.UniTrip.common.apiPayload.base.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FailureStatus implements Basecode {

    //enum 에서 상수는 필드 선언보다 위에 있어야 함.
    _BAD_REQUSET(400, "잘못된 요청입니다."),
    _USER_NOT_FOUND(400, "존재하지 않는 회원입니다."),
    _UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),
    _INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    _COMMENT_NOT_FOUND(404, "해당 댓글을 찾을 수 없습니다."),
    _POST_NOT_FOUND(404, "해당 게시글을 찾을 수 없습니다."),
    _SCHEDULE_CONFLICT(409, "동일 기간에 이미 일정이 존재합니다."),
    _INVALID_DATE_FORMAT(400, "잘못된 날짜 형식입니다."),
    _INVALID_DATE_RANGE(400, "시작일은 종료일보다 이후일 수 없습니다."),
    FORBIDDEN(403, "일정을 수정할 권한이 없습니다."),
    SCHEDULE_NOT_FOUND(404, "해당 일정을 찾을 수 없습니다.");

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

