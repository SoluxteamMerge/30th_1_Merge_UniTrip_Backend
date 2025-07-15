package com.Solux.UniTrip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_recruit_board")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
@AllArgsConstructor
public class GroupRecruitBoard {

    @Id
    private Long postId; // Board의 PK와 동일하게 사용

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Board board;

    @Column(nullable = false)
    private Boolean overnightFlag;

    @Column(nullable = false)
    private Integer recruitmentCnt;

    // 생성자
    public GroupRecruitBoard(Board board, Boolean overnightFlag, Integer recruitmentCnt) {
        this.board = board;
        this.overnightFlag = overnightFlag;
        this.recruitmentCnt = recruitmentCnt;
    }

    public void updateRecruitFields(Boolean overnightFlag, Integer recruitmentCnt) {
        this.overnightFlag = overnightFlag;
        this.recruitmentCnt = recruitmentCnt;
    }
}