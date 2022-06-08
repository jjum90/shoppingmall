package com.daou.shoppingmall.dto;

import com.daou.shoppingmall.entity.Coupon;
import com.daou.shoppingmall.entity.Point;
import com.daou.shoppingmall.entity.PointHistory;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscountContext {
    private Long memberId;
    private Coupon coupon;
    private List<PointHistory> pointHistories;
    private BigDecimal mileage;
    private BigDecimal totalPayAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscountAmount;
}
