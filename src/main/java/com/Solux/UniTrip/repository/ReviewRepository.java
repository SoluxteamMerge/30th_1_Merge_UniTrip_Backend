package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
