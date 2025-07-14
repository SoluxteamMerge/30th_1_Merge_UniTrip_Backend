package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.*;
import com.Solux.UniTrip.common.apiPayload.status.FailureStatus;
import com.Solux.UniTrip.dto.request.ScheduleCreateRequest;
import com.Solux.UniTrip.dto.request.ScheduleUpdateRequest;
import com.Solux.UniTrip.dto.response.PageResponse;
import com.Solux.UniTrip.dto.response.ScheduleListResponse;
import com.Solux.UniTrip.dto.response.ScheduleResponse;
import com.Solux.UniTrip.entity.TravelSchedule;
import com.Solux.UniTrip.entity.User;
import com.Solux.UniTrip.repository.TravelScheduleRepository;
import com.Solux.UniTrip.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
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

    //일정 생성 메소드
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

    //일정 수정 메소드
    public ScheduleResponse updateSchedule(Long scheduleId, ScheduleUpdateRequest req, User user) {
        TravelSchedule schedule = travelScheduleRepository.findById(scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);

        // 권한 체크
        if (!schedule.getUser().getUserId().equals(user.getUserId())) {
            throw new ForbiddenException();
        }

        // 날짜 유효성 검사
        if (req.getStartDate() != null && req.getEndDate() != null &&
                req.getStartDate().isAfter(req.getEndDate())) {
            throw new InvalidDateRangeException();
        }

        // 필드 업데이트 (null 아니면 덮어쓰기)
        if (req.getTitle() != null) schedule.setTitle(req.getTitle());
        if (req.getDescription() != null) schedule.setDescription(req.getDescription());
        if (req.getTravelType() != null) schedule.setTravelType(req.getTravelType());
        if (req.getStartDate() != null) schedule.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) schedule.setEndDate(req.getEndDate());
        if (req.getCompanions() != null) schedule.setCompanions(req.getCompanions());
        if (req.getIsPublic() != null) schedule.setIsPublic(req.getIsPublic());

        // 업데이트 시간 갱신
        schedule.setUpdatedAt(LocalDateTime.now());

        TravelSchedule updated = travelScheduleRepository.save(schedule);

        return ScheduleResponse.builder()
                .scheduleId(updated.getScheduleId())
                .title(updated.getTitle())
                .description(updated.getDescription())
                .travelType(updated.getTravelType())
                .startDate(updated.getStartDate())
                .endDate(updated.getEndDate())
                .companions(updated.getCompanions())
                .isPublic(updated.getIsPublic())
                .createdAt(updated.getCreatedAt())
                .updatedAt(updated.getUpdatedAt())
                .build();
    }

    //일정 삭제 메소드
    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        TravelSchedule schedule = travelScheduleRepository.findById(scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);

        //권한 체크
        if (!schedule.getUser().getUserId().equals(user.getUserId())) {
            throw new ForbiddenException();
        }

        travelScheduleRepository.delete(schedule);
    }

    //일정 상세 조회
    @Transactional(readOnly = true)
    public ScheduleResponse getScheduleDetail(Long scheduleId, User user) {
        TravelSchedule schedule = travelScheduleRepository.findById(scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);

        // 작성자와 일치하지 않아도 열람 허용
        return ScheduleResponse.of(schedule);
    }

    //일정 목록 조회
    @Transactional(readOnly = true)
    public PageResponse<ScheduleListResponse> getScheduleList(Pageable pageable) {
        Page<TravelSchedule> schedules = travelScheduleRepository.findAll(pageable);
        Page<ScheduleListResponse> dtoPage = schedules.map(ScheduleListResponse::from);
        return PageResponse.from(dtoPage);
    }




}