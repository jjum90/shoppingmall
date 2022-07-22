package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MileageRepository extends JpaRepository<Mileage, Long> {
    /**
     * 마일리지 정보 조회
     * @param mileageId
     * @return
     */
    Optional<Mileage> findById(Long mileageId);
}
