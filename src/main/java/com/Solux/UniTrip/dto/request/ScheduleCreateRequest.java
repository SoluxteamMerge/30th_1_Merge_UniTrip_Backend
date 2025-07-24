package com.Solux.UniTrip.dto.request;
//일정 생성 request

import com.Solux.UniTrip.entity.TravelSchedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleCreateRequest {
    private String title;
    private String description;
    private TravelSchedule.TravelType travelType;  // enum: 국내, 해외, 기타
    private LocalDate startDate; // yyyy-MM-dd
    private LocalDate endDate; // yyyy-MM-dd
    private String companions;
    private Boolean isPublic;
}
