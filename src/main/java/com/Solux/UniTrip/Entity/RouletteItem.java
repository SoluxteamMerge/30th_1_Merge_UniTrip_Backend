package com.Solux.UniTrip.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RouletteItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouletteItem {

    @Id
    private Integer rItemId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String rItemContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_topic_id", nullable = false)
    private RouletteTopic rouletteTopic;
}

