package com.Solux.UniTrip.repository;

import com.Solux.UniTrip.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findByKakaoId(String kakaoId); // 예: 중복 저장 방지용
}