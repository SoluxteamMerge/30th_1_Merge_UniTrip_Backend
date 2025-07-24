package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.TravelSchedule;
import com.Solux.UniTrip.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TravelScheduleRepository extends JpaRepository<TravelSchedule, Long> {

    Page<TravelSchedule> findAllByUser(User user, Pageable pageable);

    @Query("SELECT ts FROM TravelSchedule ts WHERE ts.user = :user AND " +
            "((:startDate BETWEEN ts.startDate AND ts.endDate) OR " +
            "(:endDate BETWEEN ts.startDate AND ts.endDate) OR " +
            "(ts.startDate BETWEEN :startDate AND :endDate))")
    List<TravelSchedule> findOverlappingSchedules(@Param("user") User user,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

}
