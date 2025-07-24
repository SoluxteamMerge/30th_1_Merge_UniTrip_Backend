package com.Solux.UniTrip.common.apiPayload.status;

import com.Solux.UniTrip.common.apiPayload.base.Basecode;
import com.Solux.UniTrip.common.apiPayload.base.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements Basecode {

    //enum 에서 상수는 필드 선언보다 위에 있어야 함.
    _OK(200, "요청에 성공했습니다."),
    _LOGIN_SUCCESS(200, "로그인 되었습니다."),
    _LOGOUT_SUCCESS(200, "로그아웃 되었습니다."),
    _MODIFY_USER_INFO_SUCCESS(200, "개인 정보 수정에 성공하였습니다."),
    _USER_DELETED(200, "회원 탈퇴 되었습니다."),
    _GET_USER_INFO_SUCCESS(200, "마이페이지 사용자 정보 조회에 성공하였습니다."),
    _USER_PROFILE_REGISTERED(200, "회원정보가 등록되었습니다."),
    _GET_SCRAPS_SUCCESS(200, "스크랩한 리뷰 조회에 성공하였습니다."),
    _GET_REVIEWS_SUCCESS(200, "내가 쓴 리뷰 조회에 성공하였습니다."),
    _NICKNAME_AVAILABLE(200, "사용 가능한 닉네임입니다."),
    _NICKNAME_DUPLICATED(200, "이미 사용 중인 닉네임입니다."),
    _REVIEW_LIST_EMPTY(200, "아직 작성한 리뷰가 없습니다."),
    _SCRAPS_LIST_EMPTY(200, "아직 스크랩한 리뷰가 없습니다."),
    _EMAIL_SENT_SUCCESS(200,  "인증 메일이 성공적으로 전송되었습니다."),
    _EMAIL_VERIFIED_SUCCESS(200, "이메일 인증이 완료되었습니다."),
    _REVIEW_SEARCH_SUCCESS(200, "키워드 별 리뷰 검색에 성공하였습니다."),
    _PROFILEIMAGE_UPLOAD_SUCCESS(200, "프로필 이미지 업로드에 성공하였습니다."),
    _PROFILEIMAGE_DELETE_SUCCESS(200, "프로필 이미지가 삭제되었습니다."),
    _POPULAR_SEARCH_SUCCESS(200, "선택한 인기 키워드 검색에 성공하였습니다."),
    _PLACE_FILTERING_SUCCESS(200, "지역별 게시글 목록 조회에 성공하였습니다."),
    _GET_PROFILE_SUCCESS(200, "사용자 정보 조회에 성공하였습니다."),
    _RATING_REGISTERED(200, "별점이 등록되었습니다."),
    _RATING_DELETED(200, "별점이 삭제되었습니다.");


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