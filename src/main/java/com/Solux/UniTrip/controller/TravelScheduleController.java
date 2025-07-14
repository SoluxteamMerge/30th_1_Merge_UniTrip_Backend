package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.common.jwt.JwtTokenProvider;
import com.Solux.UniTrip.dto.request.ScheduleCreateRequest;
import com.Solux.UniTrip.dto.request.ScheduleUpdateRequest;
import com.Solux.UniTrip.dto.response.PageResponse;
import com.Solux.UniTrip.dto.response.ScheduleListResponse;
import com.Solux.UniTrip.dto.response.ScheduleResponse;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.UserRepository;
import com.Solux.UniTrip.service.TravelScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    //일정 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long scheduleId,
                                            @RequestBody ScheduleUpdateRequest request,
                                            @RequestAttribute("user") User user) {
        ScheduleResponse response = travelScheduleService.updateSchedule(scheduleId, request, user);
        return ResponseEntity.ok(new ApiResponse<>(200, "일정이 성공적으로 수정되었습니다.", response));
    }

    //일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(
            @PathVariable Long scheduleId,
            @RequestAttribute("user") User user
    ) {
        travelScheduleService.deleteSchedule(scheduleId, user);
        return ResponseEntity.ok(new ApiResponse<>(200, "일정이 성공적으로 삭제되었습니다.", null));
    }

    // 일정 상세 조회 (비회원도 접근 가능)
    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getScheduleDetail(
            @PathVariable Long scheduleId,
            @RequestAttribute(name = "user", required = false) User user) {

        ScheduleResponse response = travelScheduleService.getScheduleDetail(scheduleId, user);
        return ResponseEntity.ok(new ApiResponse<>(200, "일정 상세 조회에 성공하였습니다.", response));
    }


    // 일정 목록 조회 (비회원도 접근 가능)
    @GetMapping
    public ResponseEntity<?> getScheduleList(
            @PageableDefault(size = 10) Pageable pageable) {

        PageResponse<ScheduleListResponse> response = travelScheduleService.getScheduleList(pageable);
        return ResponseEntity.ok(new ApiResponse<>(200, "일정 목록 조회에 성공하였습니다.", response));
    }


}
