package com.Solux.UniTrip.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "User")
public class UserTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // DB 컬럼명 명시
    private Long userId;          // 자바 필드명은 camelCase로

    private String name;
    private String email;
}