package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.ScheduleCreateRequest;
import com.Solux.UniTrip.dto.response.ScheduleResponse;
import com.Solux.UniTrip.repository.UserRepository;
import com.Solux.UniTrip.service.TravelScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class TravelScheduleController {

    private final TravelScheduleService travelScheduleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 일정 생성 (JWT 인증 포함)
    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleResponse>> createSchedule(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ScheduleCreateRequest request
    ) {
        String token = authorizationHeader.trim();
        String email = jwtTokenProvider.getEmailFromToken(token); // 헤더에서 email 추출
        ScheduleResponse response = travelScheduleService.createSchedule(request, email);
        return ResponseEntity.status(201).body(
                new ApiResponse<>(201, "일정이 성공적으로 생성되었습니다.", response)
        );

    }
}
