package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동증가 설정
    private Integer categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false, length = 50)
    private String categoryName;

}

