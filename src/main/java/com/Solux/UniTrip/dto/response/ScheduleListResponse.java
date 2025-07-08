package com.Solux.UniTrip.dto.response;
//일정 목록 조회 response

import com.Solux.UniTrip.entity.TravelSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ScheduleListResponse {
    private Long scheduleId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ScheduleListResponse from(TravelSchedule schedule) {
        return ScheduleListResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }
}

