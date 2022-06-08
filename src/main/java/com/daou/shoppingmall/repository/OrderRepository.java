package com.daou.shoppingmall.repository;

import java.util.List;

import com.daou.shoppingmall.dto.OrderDto;
import com.daou.shoppingmall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	/**
	 * 멤버 아이디로 결제 목록 조회
	 * @param orderId
	 * @return
	 */
	List<Order> findById(String orderId);

}
