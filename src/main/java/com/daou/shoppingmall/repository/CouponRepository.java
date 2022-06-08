package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.Coupon;
import com.daou.shoppingmall.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findById(Long couponId);
    List<Coupon> findByMemberId(Long memberId);
}
