package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "keyword_ranking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordRanking {

    @Id
    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @Column(name = "keyword", nullable = false, length = 100)
    private String keyword;

    @Column(name = "`rank`", nullable = false)
    private int rank;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @Column(name = "category_id", nullable = false)
    private int categoryId;
}

