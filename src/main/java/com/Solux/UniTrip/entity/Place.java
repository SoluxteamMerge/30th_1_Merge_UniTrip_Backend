package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.swing.plaf.synth.Region;

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
    private Long placeId;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private float lat;

    @Column(nullable = false)
    private float lng;

    @Column(nullable = false)
    private String kakaoId;

    @Column(nullable = false)
    private String categoryGroupCode;

    @Column(nullable = false)
    private String categoryGroupName;
}
