package com.Solux.UniTrip.common.apiPayload.status;

import com.Solux.UniTrip.common.apiPayload.base.Basecode;
import com.Solux.UniTrip.common.apiPayload.base.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements Basecode {

    //enum 에서 상수는 필드 선언보다 위에 있어야 함.
    _OK(200, "요청에 성공했습니다.");

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