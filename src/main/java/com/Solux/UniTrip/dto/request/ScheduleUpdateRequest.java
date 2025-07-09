package com.Solux.UniTrip.dto.request;

import com.Solux.UniTrip.entity.TravelSchedule.TravelType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//모든 필드 선택적
@Getter
@Setter
public class ScheduleUpdateRequest {
    private String title;
    private String description;
    private TravelType travelType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String companions;
    private Boolean isPublic;
}
