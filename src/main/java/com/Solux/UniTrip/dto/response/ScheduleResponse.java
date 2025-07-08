package com.Solux.UniTrip.dto.response;

import com.Solux.UniTrip.entity.TravelSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private String description;
    private TravelSchedule.TravelType travelType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String companions;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ScheduleResponse of(TravelSchedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .travelType(schedule.getTravelType())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .companions(schedule.getCompanions())
                .isPublic(schedule.getIsPublic())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

}
