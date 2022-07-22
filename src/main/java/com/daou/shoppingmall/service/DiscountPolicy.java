package com.daou.shoppingmall.service;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.utils.Money;

/**
 * 각 할인 정책에 따른 할인 정보 제공 인터페이스
 */
public interface DiscountPolicy {

    /**
     * 할인 정책에 따른 프로세서 진행
     * @param context
     * @param member
     * @param purchaseDto
     * @param policies
     * @return
     */
    default DiscountContext discountProcessor(DiscountContext context, Member member, PurchaseDto purchaseDto, DiscountPolicy... policies) {
        Money totalAmountMoney = Money.wons(purchaseDto.getTotalAmount());
        Money totalPayAmountMoney = Money.wons(purchaseDto.getTotalAmount());
        
        if(totalAmountMoney.isLessThan(Money.ZERO) || totalPayAmountMoney.isEqual(Money.ZERO)) {
            return context;
        }

        for (DiscountPolicy policy: policies) {
            if(isSatisfied(purchaseDto)) {
                context = policy.processDiscount(context, member, purchaseDto);
            }
        }
        return context;
    }

    DiscountContext processDiscount(DiscountContext context, Member member, PurchaseDto purchaseDto);

    boolean isSatisfied(PurchaseDto purchaseDto);
}
