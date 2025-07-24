package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_keywords")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchKeyword {

    @Id
    @Column(name = "keyword", length = 100)
    private String keyword;

    @Column(name = "search_count", nullable = false)
    private int searchCount;

    @Column(name = "last_searched_at")
    private LocalDateTime lastSearchedAt;
}
