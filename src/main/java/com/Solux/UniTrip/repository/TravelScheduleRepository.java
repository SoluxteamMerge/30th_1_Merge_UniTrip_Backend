package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.TravelSchedule;
import com.Solux.UniTrip.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelScheduleRepository extends JpaRepository<TravelSchedule, Long> {

    Page<TravelSchedule> findAllByUser(User user, Pageable pageable);

}
