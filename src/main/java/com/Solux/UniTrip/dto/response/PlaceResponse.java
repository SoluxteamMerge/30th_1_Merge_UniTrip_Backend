package com.Solux.UniTrip.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceResponse {

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("id")
    private int kakaoId;
}
