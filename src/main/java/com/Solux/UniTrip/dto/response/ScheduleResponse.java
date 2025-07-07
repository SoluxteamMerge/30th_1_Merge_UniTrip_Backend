package com.Solux.UniTrip.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private String startDate;
    private String endDate;
    private String description;
    private String createdAt;
}
