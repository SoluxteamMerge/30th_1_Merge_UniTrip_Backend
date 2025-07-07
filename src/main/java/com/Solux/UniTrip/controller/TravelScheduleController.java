package com.Solux.UniTrip.controller;

import com.Solux.UniTrip.common.apiPayload.base.ApiResponse;
import com.Solux.UniTrip.common.apiPayload.status.SuccessStatus;
import com.Solux.UniTrip.dto.request.ScheduleCreateRequest;
import com.Solux.UniTrip.dto.response.ScheduleResponse;
import com.Solux.UniTrip.service.TravelScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class TravelScheduleController {

    private final TravelScheduleService travelScheduleService;

    //일정 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleResponse>> createSchedule(
            @RequestBody ScheduleCreateRequest request
    ) {
        ScheduleResponse response = travelScheduleService.createSchedule(request);
        return ResponseEntity
                .status(201)
                .body(ApiResponse.onSuccess(response, SuccessStatus._OK));
    }


}
