package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	/**
	 * 주문 아이디로 결제 목록 조회
	 * @param orderId
	 * @return
	 */
	@Query("SELECT o " +
			"FROM Order o " +
			"LEFT JOIN FETCH OrderProduct op " +
			"LEFT JOIN FETCH Product p " +
			"LEFT JOIN FETCH Coupon c " +
			"LEFT JOIN FETCH PointHistory ph " +
			"WHERE o.id =: orderId")
	Optional<Order> findById(Long orderId);

}
