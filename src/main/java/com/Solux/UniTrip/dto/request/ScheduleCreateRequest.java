package com.Solux.UniTrip.dto.request;
//일정 생성 request

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleCreateRequest {
    private String title;
    private String startDate; // yyyy-MM-dd
    private String endDate;   // yyyy-MM-dd
    private String description;
}
