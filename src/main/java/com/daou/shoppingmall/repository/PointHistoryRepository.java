package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    PointHistory findById(String pointHisId);
}
