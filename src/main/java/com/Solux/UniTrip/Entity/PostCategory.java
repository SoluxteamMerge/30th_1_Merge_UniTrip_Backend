package com.Solux.UniTrip.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PostCategory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCategory {

    @Id
    private Integer categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Board.BoardType boardType;  // BoardType 재사용 가능

    @Column(nullable = false, length = 50)
    private String categoryName;
}

