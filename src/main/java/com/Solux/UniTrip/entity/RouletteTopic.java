package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RouletteTopic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouletteTopic {

    @Id
    private Integer rTopicId;

    @Column(length = 50, nullable = false)
    private String rTopicName;

    // 양방향 관계가 필요하다면 추가 가능
    // @OneToMany(mappedBy = "rouletteTopic")
    // private List<RouletteItem> items;
}
