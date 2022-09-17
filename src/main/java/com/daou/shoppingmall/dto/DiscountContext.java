package com.daou.shoppingmall.dto;

import com.daou.shoppingmall.entity.Coupon;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.entity.PointHistory;
import com.daou.shoppingmall.utils.Money;
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

    public static DiscountContext getDefaultDiscountContext(Member member, PurchaseDto purchaseDto) {
        return DiscountContext.builder()
                .memberId(member.getId())
                .totalAmount(purchaseDto.getTotalAmount())
                .totalPayAmount(purchaseDto.getTotalAmount())
                .build();
    }

    public static DiscountContext save(DiscountContext context, Coupon useCoupon, Money totalAmount, Money disCountAmount) {
        return DiscountContext.builder()
                .coupon(useCoupon)
                .totalAmount(context.getTotalAmount())
                .totalPayAmount(totalAmount.minus(disCountAmount).getAmount())
                .totalDiscountAmount(disCountAmount.getAmount())
                .mileage(context.getMileage())
                .pointHistories(context.getPointHistories())
                .memberId(context.getMemberId())
                .build();
    }

}
