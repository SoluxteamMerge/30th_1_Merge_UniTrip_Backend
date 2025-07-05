package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orgId;

    @Column(nullable = false, length = 100)
    private String orgName;

    @Column(nullable = false)
    private Long orgRegistrationNumber;

    @Column(nullable = false, length = 50)
    private String representativeName;

    @Column(nullable = false, length = 100)
    private String orgEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
