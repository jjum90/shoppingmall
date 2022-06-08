package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.dto.OrderDto;
import com.daou.shoppingmall.entity.Coupon;
import com.daou.shoppingmall.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findById(Long pointId);
    List<Point> findByMemberId(Long memberId);
}
