package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.GroupRecruitBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRecruitBoardRepository extends JpaRepository<GroupRecruitBoard, Long> {
}
