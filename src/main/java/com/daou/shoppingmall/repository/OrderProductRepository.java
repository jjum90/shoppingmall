package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
