package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    /**
     * 포인트 히스토리 아이디로 조회
     * @param pointHisId
     * @return
     */
    PointHistory findById(String pointHisId);
}
