package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.BaseException;
import com.Solux.UniTrip.common.apiPayload.exception.InvalidDateFormatException;
import com.Solux.UniTrip.common.apiPayload.exception.InvalidDateRangeException;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.dto.request.ScheduleCreateRequest;
import com.Solux.UniTrip.dto.response.ScheduleResponse;
import com.Solux.UniTrip.entity.TravelSchedule;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.TravelScheduleRepository;
import com.Solux.UniTrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class TravelScheduleService {

    private final TravelScheduleRepository travelScheduleRepository;
    private final UserRepository userRepository;


    public ScheduleResponse createSchedule(ScheduleCreateRequest req, String email) {
        // 날짜 파싱
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = req.getStartDate();
            endDate = req.getEndDate();
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException();
        }

        // 이메일 기반 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(FailureStatus._USER_NOT_FOUND));

        // 일정 생성
        TravelSchedule schedule = TravelSchedule.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .travelType(req.getTravelType())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .companions(req.getCompanions())
                .isPublic(req.getIsPublic())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        TravelSchedule saved = travelScheduleRepository.save(schedule);

        return ScheduleResponse.builder()
                .scheduleId(saved.getScheduleId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .travelType(saved.getTravelType())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .companions(saved.getCompanions())
                .isPublic(saved.getIsPublic())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}