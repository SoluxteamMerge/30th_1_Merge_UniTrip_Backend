package com.Solux.UniTrip.service;

import com.Solux.UniTrip.common.apiPayload.exception.InvalidDateFormatException;
import com.Solux.UniTrip.common.apiPayload.exception.InvalidDateRangeException;
import com.Solux.UniTrip.dto.request.ScheduleCreateRequest;
import com.Solux.UniTrip.dto.response.ScheduleResponse;
import com.Solux.UniTrip.entity.TravelSchedule;
import com.Solux.UniTrip.repository.TravelScheduleRepository;
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

    public ScheduleResponse createSchedule(ScheduleCreateRequest request) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(request.getStartDate(), dateFormatter);
            endDate = LocalDate.parse(request.getEndDate(), dateFormatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException();
        }

        TravelSchedule schedule = TravelSchedule.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDateTime.now())
                .travelType(TravelSchedule.TravelType.기타)
                .isPublic(true)
                .companions(null)
                .user(null)
                .build();

        TravelSchedule saved = travelScheduleRepository.save(schedule);

        return ScheduleResponse.builder()
                .scheduleId(saved.getScheduleId())
                .title(saved.getTitle())
                .startDate(saved.getStartDate().toString())
                .endDate(saved.getEndDate().toString())
                .description(saved.getDescription())
                .createdAt(saved.getCreatedAt().toString())
                .build();
    }
}
