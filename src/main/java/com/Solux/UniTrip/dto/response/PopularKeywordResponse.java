package com.Solux.UniTrip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularKeywordResponse {
    private String keyword;
    private int rank;
    private int searchCount;
}
