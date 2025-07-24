package com.Solux.UniTrip.dto.response;

import com.Solux.UniTrip.entity.Place;
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

    @JsonProperty("region")
    private Place.Region region;

    @JsonProperty("category_group_name")
    private String categoryGroupName;

    public static PlaceResponse from(Place place) {
        return PlaceResponse.builder()
                .placeName(place.getPlaceName())
                .address(place.getAddress())
                .categoryGroupName(place.getCategoryGroupName())
                .region(place.getRegion())
                .build();
    }
}
