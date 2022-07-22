package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    /**
     * 포인트 아이디로 조회
     * @param pointId
     * @return
     */
    Optional<Point> findById(Long pointId);
}
