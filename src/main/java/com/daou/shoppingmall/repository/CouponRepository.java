package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    /**
     * 쿠폰 정보 조회
     * @param couponId
     * @return
     */
    Optional<Coupon> findById(Long couponId);
}
