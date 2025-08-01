package com.Solux.UniTrip.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardUpdateResponse {

    private int status;          // 200 등 상태 코드
    private Long postId;         // 수정된 게시글 ID
    private String message;      // 성공/에러 메시지
    private LocalDateTime updatedAt; // 수정 시간
}
