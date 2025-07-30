package com.Solux.UniTrip.entity;

import com.Solux.UniTrip.dto.response.PlaceResponse;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="place_id")
    private Long placeId;

    @Column(name="place_name", nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String address;

//    @Column(nullable = false)
//    private float lat;
//
//    @Column(nullable = false)
//    private float lng;

    @Column(name="kakao_id", nullable = false)
    private String kakaoId;

    @Column(name="category_group_name")
    private String categoryGroupName;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Region region;

    public enum Region{
        SEOUL,
        INCHEON,
        DAEJEON,
        DAEGU,
        GWANGJU,
        BUSAN,
        ULSAN,
        SEJONG,
        GYEONGGI,
        GANGWON,
        CHUNGBUK,
        CHUNGNAM,
        GYEONGBUK,
        GYEONGNAM,
        JEONBUK,
        JEONNAM,
        JEJU,
        ETC;

        @JsonCreator
        public static Region from(String value) {
            try {
                return Region.valueOf(value.toUpperCase());
            } catch (Exception e) {
                return Region.ETC;
            }
        }
    }
}
